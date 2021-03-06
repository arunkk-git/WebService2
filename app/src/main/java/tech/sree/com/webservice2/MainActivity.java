package tech.sree.com.webservice2;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import Parser.JSON_Perser;
import model.Flower;
import model.FlowerAdapter;

public class MainActivity extends AppCompatActivity {

    //    TextView textView;
    ProgressBar progressBar;
    List<Flower> flowerList;
    String BASE_URL = "http://services.hanselandpetal.com/photos/";
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //      textView = (TextView) findViewById(R.id.textview);
        //    textView.setMovementMethod(new ScrollingMovementMethod());
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.getData:
                String Url_xml  = "http://services.hanselandpetal.com/feeds/flowers.xml";
                String Url_json  ="http://services.hanselandpetal.com/feeds/flowers.json";
                // String Url  = "file:///sdcard/Download/aaa/flowers.xml";
                new MyTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Url_json);
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void  updateInfo( String data){
        //textView.append("\n "+data);
    }

    class  MyTask extends AsyncTask<String, String ,List<Flower>>{

        @Override
        protected void onPreExecute() {
            //updateInfo(" Task Started ...");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Flower> doInBackground(String... params) {
            String data =requestData(params[0]);
            // List<Flower> flowerList  =  XML_Parser.parseFeed(data);
            List<Flower> flowerList  = JSON_Perser.parseFeed(data);
//            for(Flower flower: flowerList){
//                try {
//                    String fullURL = BASE_URL+flower.getPhoto();
//                    InputStream iStream = (InputStream) new  URL(fullURL).getContent();
//                    flower.setBitmap(BitmapFactory.decodeStream(iStream));
//                    iStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            return flowerList;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateInfo(values[0]);
        }

        @Override
        protected void onPostExecute(List<Flower> data) {
            progressBar.setVisibility(View.INVISIBLE);
            //for(Flower flower:data)            updateInfo(flower.getName());
            FlowerAdapter flowerAdapter =  new FlowerAdapter(MainActivity.this,R.layout.single_row,data);
            listView.setAdapter(flowerAdapter);
            //for(Flower flower :flowerList)             updateInfo(flower.getName());

        }
    }
    private boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return  true;
        else {
            Toast.makeText(getApplicationContext(),"NetWork Connectivity is NOT avaliable",Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private String requestData(String Url){
        String data = null;
        if(isOnLine())
            data =  NetWorkManager.getData(Url);//
        return data ;
    }
}
