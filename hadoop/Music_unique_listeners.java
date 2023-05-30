// Isme when you copy the dataset from linux to hadoop...make sure that u delete the column names...
// if column names are not deleted then you will face an error "NumberFormatException"

package make2package;

import java.io.IOException;
import java.text.ParseException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class make2class {
    public static void main(String [] args) throws Exception{
                Configuration c = new Configuration();
                String[] files = new GenericOptionsParser(c,args).getRemainingArgs();
                Path input=new Path(files[0]);
                Path output=new Path(files[1]);
                Job j=new Job(c,"WordCount");
                j.setJarByClass(make2class.class);
                j.setMapperClass(MapForWordCount.class);
                j.setReducerClass(ReduceForWordCount.class);
                j.setOutputKeyClass(Text.class);
                j.setOutputValueClass(IntWritable.class);
                FileInputFormat.addInputPath(j, input);
                FileOutputFormat.setOutputPath(j, output);
                System.exit(j.waitForCompletion(true)?0:1);
        }

    public static class MapForWordCount extends Mapper<LongWritable, Text, Text, IntWritable> {

        public void map(LongWritable key, Text value, Context con) throws IOException, InterruptedException {


			String tmp = value.toString();
			String [] lines = tmp.split("\n");
			for (String line: lines){
				
				String[] eachLine = line.split(",");
				String userId=eachLine[0];
//				String trackId = eachLine[1];
//				String isShared = eachLine[2];
//				if(Integer.parseInt(isShared) != 0){
//					con.write(new Text(trackId), new IntWritable(1));
//				}
				con.write(new Text(userId),new IntWritable(0));
			} 

        }
    }

    public static class ReduceForWordCount extends Reducer<Text, IntWritable, Text, IntWritable> {
    	int count = 0;

    	
        public void reduce(Text key, Iterable<IntWritable> values, Context con) throws IOException, InterruptedException {
			count++;
			con.write(key, new IntWritable(1));
			
        }

        
        @Override 
        protected void cleanup(Context context) throws IOException, InterruptedException { 
              context.write(new Text("Number of distinct users are : "),new IntWritable(count)); 
              
            	  
              
               
        } 
    }
}
