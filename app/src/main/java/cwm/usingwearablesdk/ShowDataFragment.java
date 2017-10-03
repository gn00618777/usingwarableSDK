package cwm.usingwearablesdk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by user on 2017/9/10.
 */

public class ShowDataFragment extends Fragment {
    //UI
    private View mView;
    private TextView countStepView;
    private TextView distanceView;
    private TextView caloriesView;
    private TextView statusView;
    private TextView heartView;
    private int mStep = 0;
    private int mDistance = 0;
    private int mCalories = 0;
    private int mHeart = 0;
    private String mGesture = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_count_step, null);
        }

        countStepView = (TextView)mView.findViewById(R.id.step_count);
        distanceView = (TextView)mView.findViewById(R.id.distance);
        caloriesView = (TextView)mView.findViewById(R.id.calories);
        heartView = (TextView)mView.findViewById(R.id.heart_beat);
        statusView = (TextView)mView.findViewById(R.id.gesture);

        countStepView.setText("Step: "+Integer.toString(mStep));
        distanceView.setText("Distance: "+Integer.toString(mDistance));
        caloriesView.setText("Calories: "+Integer.toString(mCalories));
        heartView.setText("HeartBeat: "+Integer.toString(mHeart));
        statusView.setText("Gesture: "+mGesture);


        return mView;
    }
    public void setValue(int step, int distance, int calories, String gesture){
        mStep = step;
        mDistance = distance;
        mCalories = calories;
        mGesture = gesture;
    }
    public void setHeartValue(int heart){
        mHeart = heart;
    }
}
