package com.example.votingapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionDetailFragment extends Fragment {

    private Question mQuestion;
    private AnswerListAdapter mAdapter;
    private TextView mVoteCount;
    private ValueEventListener mVoteCountListener;
    private Query mVoteCountQuery;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private ViewPager2 mViewPager;
    private Group mGroup;

    public QuestionDetailFragment() {
        super(R.layout.fragment_question_detail);
    }

    public interface OnViewReadyListener {
        void onViewReady();
    }

    private OnViewReadyListener listener;

    public void setOnViewReadyListener(OnViewReadyListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_detail, container, false);

        mVoteCount = view.findViewById(R.id.tv_total_vote_count);
//        mBarChart = view.findViewById(R.id.bar_chart);
        mPieChart = new PieChart(getContext());
        mBarChart = new BarChart(getContext());
        mViewPager = view.findViewById(R.id.view_pager);

        Bundle bundle = getArguments();
        assert bundle != null;
        mQuestion = (Question) bundle.getSerializable("question");
        mGroup = (Group) bundle.getSerializable("group");
        ArrayList<Answer> answers = (ArrayList<Answer>) mQuestion.getAnswers();

        RecyclerView answerRecyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        answerRecyclerView.setLayoutManager(layoutManager);
        answerRecyclerView.setHasFixedSize(true);

        mAdapter = new AnswerListAdapter(answers, mQuestion, mGroup,  getContext());
        mAdapter.setIsInDetailsView(true);
        answerRecyclerView.setAdapter(mAdapter);

        ImageButton graphButton = view.findViewById(R.id.button);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) graphButton.getLayoutParams();
                int currentPosition = mViewPager.getCurrentItem();
                Drawable drawable;

                if (currentPosition == 0) {
                    mViewPager.setCurrentItem(1);
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back, null);
                    layoutParams.horizontalBias = 0.0f;
                } else {
                    mViewPager.setCurrentItem(0);
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_forward, null);
                    layoutParams.horizontalBias = 1.0f;
                }
                mViewPager.getAdapter().notifyDataSetChanged();


                graphButton.setLayoutParams(layoutParams);
                graphButton.setImageDrawable(drawable);
            }
        });

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ImageButton graphButton = requireView().findViewById(R.id.button);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) graphButton.getLayoutParams();
                Drawable drawable;
                if (position == 0) {
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_forward, null);
                    layoutParams.horizontalBias = 1.0f;
                } else {
                    drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back, null);
                    layoutParams.horizontalBias = 0.0f;
                }
                graphButton.setLayoutParams(layoutParams);
                graphButton.setImageDrawable(drawable);
            }
        });

        updateVoteCount();
//        updateBarChart(mQuestion.getAnswers());

        return view;
    }

    public void updateVoteCount() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference();

        // Query to set a listener on the answers
        mVoteCountQuery = rootRef.child("Group")
                .child(mQuestion.getGroupId())
                .child("Question")
                .child(mQuestion.getId())
                .child("answers");

        mVoteCountListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalVoteCount = 0;
                ArrayList<Answer> answers = new ArrayList<>();
                for (DataSnapshot answerSnapshot : snapshot.getChildren()) {
                    Answer answer = answerSnapshot.getValue(Answer.class);
                    assert answer != null;
                    answers.add(answer);
                    answer.setGroupEncryptionKey(mGroup.getGroupEncryptionKey());
                    totalVoteCount += answer.getVotesAsInt();
                }
                String voteCountText = totalVoteCount + " votes";
                mVoteCount.setText(voteCountText);
                mQuestion.setAnswers(answers);
//
//                PieChart pieChart = new PieChart(getContext());
//                mBarChart = new BarChart(getContext());

                updateBarChart(answers);
                updatePieChart(answers);
                mViewPager.setAdapter(new ChartPagerAdapter(mBarChart, mPieChart));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Vote count could not be updated", Toast.LENGTH_SHORT).show();

            }
        };

        mVoteCountQuery.addValueEventListener(mVoteCountListener);
    }
    private List<GradientColor> barColors = new ArrayList<>();

    private void updateBarChart(List<Answer> answers) {

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int lowestValue = 0;
        int highestValue = 0;
        int i = 0;

//        if (barColors.isEmpty()) {
//            barColors = getGradientColors(answers.size());
//        }

        for (Answer answer : answers) {
            answer.setGroupEncryptionKey(mGroup.getGroupEncryptionKey());
            entries.add(new BarEntry(i, answer.getVotesAsInt()));
            labels.add(answer.getDecryptedAnswerTitle());
            if (lowestValue == 0 || answer.getVotesAsInt() < lowestValue) {
                lowestValue = answer.getVotesAsInt();
            }
            if (highestValue == 0 || answer.getVotesAsInt() > highestValue) {
                highestValue = answer.getVotesAsInt();
            }

            i++;
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
//        barDataSet.setGradientColors(barColors);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // Create a bar data object
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.6f);
        barData.setDrawValues(true);
        barData.setValueTextSize(12f);
        barData.setValueTextColor(Color.parseColor("#FFFFFF"));
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        mBarChart.setData(barData);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        mBarChart.getXAxis().setTextColor(Color.parseColor("#FFFFFF"));
        mBarChart.getXAxis().setDrawLabels(true);
        mBarChart.getXAxis().setDrawGridLines(false);
        mBarChart.getXAxis().setGranularity(1f);
        mBarChart.getXAxis().setGranularityEnabled(true);
//        mBarChart.setScaleX(1);
        mBarChart.getAxisRight().setDrawLabels(false);
        mBarChart.getAxisRight().setDrawGridLines(false);
        mBarChart.getAxisLeft().setTextColor(Color.parseColor("#FFFFFF"));
//        mBarChart.getAxisLeft().setGranularity(0.5f);
        mBarChart.getAxisLeft().setAxisMinimum(lowestValue - 1f);
        mBarChart.getAxisLeft().setAxisMaximum(highestValue + 1f);
        mBarChart.getAxisLeft().setGranularityEnabled(true);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setMinimumHeight(600);
        mBarChart.animateY(1000);
        mBarChart.invalidate();
    }

    private void updatePieChart(List<Answer> answers) {
        List<PieEntry> entries = new ArrayList<>();
        for (Answer answer : answers) {
            entries.add(new PieEntry(answer.getVotesAsInt(), answer.getDecryptedAnswerTitle()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Answers");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        mPieChart.setData(data);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setCenterText("Answers");
        mPieChart.setCenterTextSize(20f);
        mPieChart.setCenterTextColor(Color.parseColor("#FFFFFF"));
        mPieChart.setHoleRadius(30f);
        mPieChart.setTransparentCircleRadius(0f);
        mPieChart.setDrawEntryLabels(false);
        mPieChart.setDrawMarkers(false);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setUsePercentValues(true);
        mPieChart.setEntryLabelTextSize(12f);
        mPieChart.setEntryLabelColor(Color.parseColor("#FFFFFF"));
        mPieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        mPieChart.setEntryLabelColor(Color.parseColor("#FFFFFF"));
        mPieChart.setFilterTouchesWhenObscured(true);
        mPieChart.setMinimumHeight(550);
        mPieChart.animateY(1000);
        mPieChart.getLegend().setEnabled(false);
        mPieChart.setTouchEnabled(true);
        mPieChart.invalidate();
    }




    private List<GradientColor> getGradientColors(int size) {
        List<GradientColor> gradientColors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            gradientColors.add(new GradientColor(getRandomColor(), getRandomColor()));
        }
        return gradientColors;
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }





    @Override
    public void onStop() {
        super.onStop();
        mVoteCountQuery.removeEventListener(mVoteCountListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mVoteCountQuery.addValueEventListener(mVoteCountListener);
    }




}
