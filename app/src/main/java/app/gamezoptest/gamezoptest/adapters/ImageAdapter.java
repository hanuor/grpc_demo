package app.gamezoptest.gamezoptest.adapters;
/*
 * Created by Han
 *Vamos!
 *
 */

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import app.gamezoptest.gamezoptest.Models.AdapterModel;
import app.gamezoptest.gamezoptest.R;

public class ImageAdapter extends ArrayAdapter<AdapterModel> {

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    ArrayList<AdapterModel> dataList = new ArrayList<AdapterModel>();
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    public ImageAdapter(Context context, int textViewResourceId,
                        ArrayList<AdapterModel> objects) {
        super(context, textViewResourceId, objects);
        dataList = objects;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRandom = new Random();
    }
    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.dynamic_row,
                    parent, false);
            vh = new ViewHolder();
            vh.imgView = (DynamicHeightImageView) convertView
                    .findViewById(R.id.imgView);
            vh.textView = (TextView) convertView
                    .findViewById(R.id.textView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);

        vh.imgView.setHeightRatio(positionHeight);
        vh.textView.setText(dataList.get(position).name);
        Picasso.with(getContext()).load(dataList.get(position).URL).into(vh.imgView);
        return convertView;
    }

    static class ViewHolder {
        DynamicHeightImageView imgView;
        TextView textView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0;
    }
}
