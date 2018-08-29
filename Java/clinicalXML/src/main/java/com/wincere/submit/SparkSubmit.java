package com.wincere.submit;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;

public class SparkSubmit {
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
		"/home/himanshu/Projects/Spark_1.2.0/Java/clinicalXML/target/clinical_element_120-1.0-jar-with-dependencies.jar",
		"--class", "com.wincere.store.ClinicalXML" };

SparkConf conf = new SparkConf();
ClientArguments c1 = new ClientArguments(args1, conf);
new Client(c1, config, conf).run();

}
}
