/*package com.wincere.analytics.submit;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.ClientArguments;
import org.apache.spark.deploy.yarn.Client;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class YARNSubmit implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		// TODO Auto-generated method stub
		System.setProperty("SPARK_YARN_MODE", "true");
		Configuration config = new Configuration();
		config.set("fs.default.name","hdfs://172.16.1.64:9000");
		config.set("yarn.resourcemanager.address","172.16.1.64:8032");
		config.set("yarn.resourcemanager.scheduler.address","172.16.1.64:8030");
		config.set("yarn.resourcemanager.resource-tracker.address","172.16.1.64:8031");
		config.set("yarn.resourcemanager.hostname","172.16.1.64");
		SparkConf conf = new SparkConf();
		String []args = {"--jar","/home/himanshu/Projects/Spark_1.2.0/Java/SqlTransformation/target/SqlTransformations_120-1.0-jar-with-dependencies.jar","--class","com.wincere.analytics.SparkApplicationMaster"};
	    ClientArguments c1=new ClientArguments(args, conf);
	    new Client(c1, config, conf).run();
		return RepeatStatus.FINISHED;
	}
	
	
	
}
*/

package com.wincere.analytics.submit;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.ClientArguments;
import org.apache.spark.deploy.yarn.Client;

public class YARNSubmit {
	public static void main(String[] args) throws InterruptedException,
			YarnException, IOException {

		System.setProperty("SPARK_YARN_MODE", "true");
		Configuration config = new Configuration();
		config.set("fs.default.name", "hdfs://172.16.1.64:9000");
		config.set("yarn.resourcemanager.address", "172.16.1.64:8032");
		config.set("yarn.resourcemanager.scheduler.address", "172.16.1.64:8030");
		config.set("yarn.resourcemanager.resource-tracker.address",
				"172.16.1.64:8031");
		config.set("yarn.resourcemanager.hostname", "172.16.1.64");

		String[] args1 = {
				"--jar",
				"/home/himanshu/Projects/Spark_1.2.0/Java/RWS_ETL/target/RWS_ETL_120-1.0-jar-with-dependencies.jar",
				"--class", "com.wincere.analytics.StreamingApplication" };

		SparkConf conf = new SparkConf();
		ClientArguments c1 = new ClientArguments(args1, conf);
		new Client(c1, config, conf).run();

	}

}