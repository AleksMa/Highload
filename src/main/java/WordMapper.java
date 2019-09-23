import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String str = value.toString();
        str = str.replaceAll("(\\p{Punct}|«|»|…)", "");
        String[] words = str.split("\\p{Space}");
        for (String word : words) {
            word = word.toLowerCase();
            context.write(new Text(word), new IntWritable(1));
        }
    }
}