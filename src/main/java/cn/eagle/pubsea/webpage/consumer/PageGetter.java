package cn.eagle.pubsea.webpage.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import cn.eagle.pubsea.webpage.proto.WebPageProto;
import cn.eagle.pubsea.webpage.proto.WebPageProto.WebPageMessage;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class PageGetter {
	private static final Logger logger = LoggerFactory.getLogger(DbSyncer.class); 
	private static String zkList = "iZ11x0a6dv7Z:2181";//"139.196.56.177:2181";
	private static String topic = "test_data";//vivien
	private static boolean storInDB = true;
	private static boolean storInFile = true;
	
	public static void main(String[] args) {
		for (int i = 1; i < args.length; i++) {
			switch (args[i].toLowerCase()) {
			case "-zk":
				zkList = args[++i];
				break;

			case "-topic":
			case "-tp":
				topic = args[++i];
				break;

			case "-s":
				int index = ++i;
				if (args[index].equals("file")) {
					storInDB = false;
				}else if (args[index].equals("db")) {
					storInFile = false;
				}
				break;

			case "-dbip":
				DbSyncer.setDbIp(args[++i]);
				break;

			case "-dpport":
				DbSyncer.setDbPort(Integer.parseInt(args[++i]));
				break;

			case "-dpname":
				DbSyncer.setDbName(args[++i]);
				break;

			case "-dpuser":
				DbSyncer.setDbUser(args[++i]);
				break;
				
			case "-dbpass":
				DbSyncer.setDbPass(args[++i]);
				break;
				
			case "-filepath":
			case "-fp":
				FileSyncer.setFolderName(args[++i]);
				break;

			default:
				break;
			}
		}

		Properties props = new Properties();
		props.put("zookeeper.connect", zkList);
		props.put("group.id", "db");
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");
		ConsumerConfig consumerConfig = new ConsumerConfig(props);
		ConsumerConnector consumer = (ConsumerConnector) Consumer.createJavaConsumerConnector(consumerConfig);

		int threadNum = 10;
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic,threadNum);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);

		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

		ExecutorService executor = Executors.newCachedThreadPool();
		for ( final KafkaStream<byte[], byte[]> stream : streams ) {
			executor.submit(new Runnable() {
				public void run() {
					DbSyncer dbSyncer = new DbSyncer();
					FileSyncer fileSyncer = new FileSyncer();

					ConsumerIterator<byte[], byte[]> iter = stream.iterator();
					while ( iter.hasNext() ) {
						MessageAndMetadata<byte[] , byte[]> mam = iter.next();
						try {
							int dbId = -1;
							WebPageMessage msg = WebPageProto.WebPageMessage.parseFrom(mam.message());
//							System.out.println("timestamphtml:" + msg.getTimestampHtml().toStringUtf8().length());
//							System.out.println("contenthtml:" + msg.getContentHtml().toStringUtf8().length());
							if (storInDB) {
								dbId = dbSyncer.storeInDB(msg);
							}
							if (storInFile) {
								fileSyncer.storeInFile(msg, dbId);
							}
						} catch (InvalidProtocolBufferException e) {
							logger.error("get msg from kafka error, msg:" + e.getMessage());
							e.printStackTrace();
						}

						System.out.println("get one");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}

		try {
			Thread.sleep(1000 * 300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		consumer.shutdown();
		executor.shutdown();
		while ( !executor.isTerminated() ) {
			try {
				executor.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		}
	}
}
