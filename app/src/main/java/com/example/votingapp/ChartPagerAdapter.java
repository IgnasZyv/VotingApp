package com.example.votingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class ChartPagerAdapter extends RecyclerView.Adapter<ChartPagerAdapter.ViewHolder> {

    private List<View> views = new ArrayList<>();

    public ChartPagerAdapter(BarChart barChart, PieChart lineChart) {
        views.add(barChart);
        views.add(lineChart);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == 0) ? R.layout.answer_bar_chart : R.layout.answer_pie_chart;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(views.get(position));
    }

    @Override
    public int getItemCount() {
        return views.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void bind(View chartView) {
            ViewGroup parent = (ViewGroup) view;
            parent.removeAllViews();
            parent.addView(chartView);

        }
    }
}
