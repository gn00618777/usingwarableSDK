package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class TabataActionItemFragment extends Fragment {

    private View mView;


    private TextView actionItemView;
    private TextView actionItemStartView;
    private TextView actionCountView;
    private TextView connectionView;
    private TextView heartView;
    private TextView strengthView;
    private TextView caloriesView;


    private Button tabataPauseButton;
    private Button tabataDoneButton;
    private Button tabataResumeButton;

    private GifImageView gifImageView;

    private String actionItem="";
    private String actionItemStart="";
    private String actionComment="";

    private boolean mConnectState = false;
    private int heartRate = 0;
    private String strength = "0";
    private String calories = "0";


    private String count = "0";

    ListenForTabataActionItemFragment mCallback;


    public interface ListenForTabataActionItemFragment {
        public void onPressTabataDoneButton();
        public void onPressTabataPauseButton();
        public void onPressTabataResumeButton();
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForTabataActionItemFragment) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForTabataActionItemFragment");
        }

    }

    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {

        } else {
            if(mConnectState == false)
                connectionView.setText("connect status: "+"斷線");
            else
                connectionView.setText("connect status: "+"連線");
            heartView.setText("heart status:"+Integer.toString(heartRate));
            actionItemView.setText(actionItem+"/"+actionComment);
            actionCountView.setText(count);
            strengthView.setText("strength status:"+strength);
            caloriesView.setText("calories status:"+calories);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_action_item, null);
        }
        connectionView = (TextView)mView.findViewById(R.id.connect_state);
        if(mConnectState == false)
          connectionView.setText("connect status: "+"斷線");
        else
            connectionView.setText("connect status: "+"連線");

        heartView = (TextView)mView.findViewById(R.id.heart_state);
        heartView.setText("heart status:"+Integer.toString(heartRate));
        strengthView = (TextView)mView.findViewById(R.id.strength_state);
        strengthView.setText("strength status:"+strength);
        caloriesView = (TextView)mView.findViewById(R.id.calories_state);
        caloriesView.setText("calories status:"+calories);
        actionItemView = (TextView)mView.findViewById(R.id.action_item_view);
        actionItemView.setText(actionItem+"/"+actionComment);
        actionItemStartView = (TextView)mView.findViewById(R.id.action_item_start_view);
        actionItemStartView.setText(actionItemStart);
        gifImageView = (GifImageView) mView.findViewById(R.id.gif_giv);
        actionCountView = (TextView)mView.findViewById(R.id.action_item_count);
        actionCountView.setText(count);
        if(actionItem.equals("Push Up")) {
            gifImageView.setBackgroundResource(R.drawable.push_up);
        }
        else if(actionItem.equals("Crunch")) {
            gifImageView.setBackgroundResource(R.drawable.crunches);
        }
        else if(actionItem.equals("Squart")) {
            gifImageView.setBackgroundResource(R.drawable.squat);
        }
        else if(actionItem.equals("Jumping Jack")) {
            gifImageView.setBackgroundResource(R.drawable.jump_jack);
        }
        else if(actionItem.equals("Dips")) {
            gifImageView.setBackgroundResource(R.drawable.dip);
        }
        else if(actionItem.equals("High Kniess Running")) {
            gifImageView.setBackgroundResource(R.drawable.high_kness_running);
        }
        else if(actionItem.equals("Lunges")) {
            gifImageView.setBackgroundResource(R.drawable.lunge);
        }
        else if(actionItem.equals("Burpees")) {
            gifImageView.setBackgroundResource(R.drawable.burpee);
        }
        else if(actionItem.equals("Step On Chair")) {
            gifImageView.setBackgroundResource(R.drawable.step_on_chair);
        }
        else if(actionItem.equals("PushUp Rotation")) {
            gifImageView.setBackgroundResource(R.drawable.push_up_rotation);
        }

        tabataPauseButton = (Button)mView.findViewById(R.id.tabata_pause);
        tabataPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressTabataPauseButton();
            }
        });


        tabataDoneButton = (Button)mView.findViewById(R.id.tabata_done);
        tabataDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressTabataDoneButton();
            }
        });

        tabataResumeButton = (Button)mView.findViewById(R.id.tabata_resume);
        tabataResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressTabataResumeButton();
            }
        });

        return mView;
    }

    public void setActionItemView(String s){
        actionItem = s;
    }
    public void setActionItemStartView(String s){
        actionItemStart = s;
    }
    public void setActionItemCommentView(String s){actionComment = s;}
    public void setActionCountView(String c){count = c;}
    public void setConnectStatus(boolean s) {mConnectState = s;}
    public void setHeartRate(int heart){heartRate = heart;}
    public void setStrength(String strength){this.strength = strength;}
    public void setCalories(String calories){this.calories = calories;}

}
