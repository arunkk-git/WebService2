package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import tech.sree.com.webservice2.R;

/**
 * Created by ananth on 8/17/2016.
 */
public class FlowerAdapter extends BaseAdapter {
    Context context;
    int resourceId;
    List<Flower> flowerList;
    private LruCache<Integer,Bitmap> ImageCache;


    public FlowerAdapter(Context context, int resourceId, List<Flower> flowerList){
        this.context = context;
        this.resourceId = resourceId;
        this.flowerList = flowerList;
        int maxMemSize  = (int) (Runtime.getRuntime().maxMemory()/1024);
        int maxCache =  maxMemSize /8;
        ImageCache = new LruCache<>(maxCache);

    }
    @Override
    public int getCount() {
        return flowerList.size();
    }

    @Override
    public Object getItem(int position) {
        return flowerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resourceId,parent,false);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(flowerList.get(position).getName());
        TextView details = (TextView) view.findViewById(R.id.details);
        details.setText(flowerList.get(position).getInstructions());
        Flower flower = flowerList.get(position);
        Bitmap bitmap = ImageCache.get(flower.getProductId());
        if(bitmap !=  null ){
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageBitmap(flower.getBitmap());

        }else{
            FlowerAndView container = new FlowerAndView();
            container.flower = flower;
            container.view = view;
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(container);
        }
        return  view;
    }

    class FlowerAndView{
        public Flower flower;
        public View view;
        public Bitmap bitmap;
    }
    String BASE_URL = "http://services.hanselandpetal.com/photos/";
    private class ImageLoader extends AsyncTask<FlowerAndView,Void,FlowerAndView>{

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {
            FlowerAndView container  = params[0];
            Flower flower = container.flower;

            try {
                String fullURL = BASE_URL+flower.getPhoto();
                InputStream iStream = (InputStream) new URL(fullURL).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(iStream);
                flower.setBitmap(bitmap);
                container.bitmap = bitmap;
                iStream.close();

                return  container;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView flowerAndView) {
            ImageView imageView = (ImageView) flowerAndView.view.findViewById(R.id.imageView);
            imageView.setImageBitmap(flowerAndView.bitmap);
        ImageCache.put(flowerAndView.flower.getProductId(),flowerAndView.bitmap);
            //flowerAndView.flower.setBitmap(flowerAndView.bitmap);
        }
    }
}
