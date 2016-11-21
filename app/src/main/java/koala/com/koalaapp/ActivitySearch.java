package koala.com.koalaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ActivitySearch extends AppCompatActivity {

    private static final String URL_WIKI = "http://www.wikihow.com/wikiHowTo?search=";

    ProgressDialog progressDialog;

    private EditText searchBox;
    private Button searchButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBox = (EditText)findViewById(R.id.searchBox);
        searchButton = (Button)findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                new FetchData().execute(searchBox.getText().toString());
            }
        });
    }

    //getting data from wiki-how
    private class FetchData extends AsyncTask<String, Void, Void>
    {
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> urls = new ArrayList<String>();

        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivitySearch.this);
            progressDialog.setTitle("Crawler");
            progressDialog.setMessage("Fetching data...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        protected Void doInBackground(String... params)
        {
            String query = params[0];
            try
            {
                Connection connection = Jsoup.connect(URL_WIKI + query)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) " +
                                   "AppleWebKit/535.21 (KHTML, like Gecko) " +
                                   "Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000);
                Connection.Response response = connection.execute();
                if(response.statusCode()==200)
                {
                    Document html = connection.get();
                    Elements elements = html.getElementsByClass("result_link");
                    for(Element element : elements)
                    {
                        list.add(element.text());
                        urls.add(element.attr("href"));
                    }
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            LinearLayout layout = (LinearLayout)findViewById(R.id.resultbox);
            layout.removeAllViews();
            for(int i=0;i<list.size();i++)
            {
                addViews(layout, list.get(i), urls.get(i));
            }
            progressDialog.dismiss();
        }

        private void addViews(LinearLayout layout, String text, final String url)
        {
            final TextView textView = new TextView(ActivitySearch.this);
            textView.setText(text);
            textView.setTextSize(22);

            textView.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    Intent intent = new Intent(ActivitySearch.this, ActivityTask.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            layout.addView(textView);
        }
    }
}