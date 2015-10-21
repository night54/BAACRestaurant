package th.or.baac.oa.baacrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by BAAC on 10/21/2015.
 */
public class MyFoodAdapter extends BaseAdapter {
    // Explicit
    private Context objContext;
    private String[] sourceStrings, foodStrings, priceStrings;

    public MyFoodAdapter(Context objContext, String[] sourceStrings, String[] foodStrings, String[] priceStrings) {
        this.objContext = objContext;
        this.sourceStrings = sourceStrings;
        this.foodStrings = foodStrings;
        this.priceStrings = priceStrings;
    }

    @Override
    public int getCount() {
        return foodStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View objView = objLayoutInflater.inflate(R.layout.food_listview, viewGroup, false);

        // show food
        TextView foodTextView = (TextView) objView.findViewById(R.id.txtShowFood);
        foodTextView.setText(foodStrings[i]);

        // show price
        TextView priceTextView = (TextView) objView.findViewById(R.id.txtShowPrice);
        priceTextView.setText(priceStrings[i]);

        // for icon
        ImageView iconImageView = (ImageView) objView.findViewById(R.id.imvIcon);
        Picasso.with(objContext).load(sourceStrings[i]).resize(120, 120).into(iconImageView);

        return objView;
    }
} // End of Main Class
