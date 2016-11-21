package koala.com.koalaapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityTask extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

        new FetchTasks().execute(url);
    }

    //asynchronous task
    private class FetchTasks extends AsyncTask<String, Void, Void>
    {
        ArrayList<String> tasks = new ArrayList<String>();
        String taskString = "";

        protected void onPreExecute(){}

        protected Void doInBackground(String... params)
        {
            String url = "http://" + params[0].substring(2);
            try
            {
                Connection connection = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) " +
                                "AppleWebKit/535.21 (KHTML, like Gecko) " +
                                "Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000);
                Connection.Response response = connection.execute();
                if(response.statusCode()==200)
                {
                    Document html = connection.get();
                    Elements elements = html.getElementsByClass("whb");
                    for(Element element : elements)
                    {
                        tasks.add(element.text());
                    }
                }
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            for(String task : tasks)
            {
                taskString += task + "\n \n";
            }
            TextView results = (TextView)findViewById(R.id.tasklist);
            results.setText(taskString);
        }
    }
}