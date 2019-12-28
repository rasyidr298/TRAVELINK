package com.coswick.travelinktrial.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.coswick.travelinktrial.R;
import com.coswick.travelinktrial.activity.DetailWIsata;
import com.coswick.travelinktrial.model.FavoriteModel;
import com.coswick.travelinktrial.model.WisataWonogiriModel;
import com.coswick.travelinktrial.model.WisataYogyaModel;
import com.coswick.travelinktrial.wisata.WisataWonogiri;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WisataWonogiriAdapter extends RecyclerView.Adapter<WisataWonogiriAdapter.ViewHolder> implements Filterable {

    List<WisataWonogiriModel> wisataWonogiriModels;
    Context ct;
    private List<WisataWonogiriModel> exampleListFull;

    public WisataWonogiriAdapter(List<WisataWonogiriModel> product_models, Context ct) {
        this.wisataWonogiriModels = product_models;
        this.ct = ct;
        exampleListFull = new ArrayList<>(product_models);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wisata,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final WisataWonogiriModel productList= wisataWonogiriModels.get(i);
        String pimg=productList.getImage();

        viewHolder.tv_nama.setText(wisataWonogiriModels.get(i).getTitle());
        viewHolder.tv_kat.setText(wisataWonogiriModels.get(i).getKategori());
        viewHolder.tv_desc.setText(wisataWonogiriModels.get(i).getDesc());
        viewHolder.tv_harga.setText(wisataWonogiriModels.get(i).getHarga());
        Picasso.with(ct).load(wisataWonogiriModels.get(i).getImage());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, DetailWIsata.class);
                //passing data to the book activity
                intent.putExtra("title", wisataWonogiriModels.get(i).getTitle());
                intent.putExtra("img", wisataWonogiriModels.get(i).getImage());
                intent.putExtra("kat", wisataWonogiriModels.get(i).getKategori());
                intent.putExtra("desc", wisataWonogiriModels.get(i).getDesc());
                intent.putExtra("harga", wisataWonogiriModels.get(i).getHarga());

                //start the activity
                ct.startActivity(intent);

            }
        });
        Picasso.with(ct).load(pimg).into(viewHolder.img);
        viewHolder.tv_nama.setText(productList.getTitle());
        viewHolder.tv_desc.setText(productList.getDesc());
        viewHolder.tv_kat.setText(productList.getKategori());
        viewHolder.tv_harga.setText(productList.getHarga());

        if (WisataWonogiri.favoriteDatabase_wonogiri.favoriteDao().isFavorite(productList.getId())==1)
            viewHolder.fav_btn.setImageResource(R.drawable.ic_favorite);
        else
            viewHolder.fav_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        viewHolder.fav_btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                FavoriteModel favoriteModel =new FavoriteModel();

                int id=productList.getId();
                String image=productList.getImage();
                String name=productList.getTitle();
                String kategori=productList.getKategori();
                String deskripsi=productList.getDesc();
                String harga=productList.getHarga();

                favoriteModel.setId(id);
                favoriteModel.setKategori(kategori);
                favoriteModel.setImage(image);
                favoriteModel.setName(name);
                favoriteModel.setDeskripsi(deskripsi);
                favoriteModel.setHarga(harga);

                if (WisataWonogiri.favoriteDatabase_wonogiri.favoriteDao().isFavorite(id)!=1){
                    viewHolder.fav_btn.setImageResource(R.drawable.ic_favorite);
                    WisataWonogiri.favoriteDatabase_wonogiri.favoriteDao().addData(favoriteModel);

                }else {
                    viewHolder.fav_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    WisataWonogiri.favoriteDatabase_wonogiri.favoriteDao().delete(favoriteModel);

                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return wisataWonogiriModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img,fav_btn;
        TextView tv_nama,tv_kat,tv_desc,tv_harga;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.img_pr);
            tv_nama=(TextView)itemView.findViewById(R.id.tv_name);
            tv_kat=(TextView)itemView.findViewById(R.id.kat);
            tv_desc=(TextView)itemView.findViewById(R.id.desc);
            tv_harga=(TextView)itemView.findViewById(R.id.hrg);
            fav_btn=(ImageView)itemView.findViewById(R.id.fav_btn);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<WisataWonogiriModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (WisataWonogiriModel item : exampleListFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            wisataWonogiriModels.clear();
            wisataWonogiriModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}