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
import android.widget.ImageView;
import android.widget.TextView;

public class TabataIntervalFragment extends Fragment {

    private View mView;

    private TextView intervalStartView;
    private TextView intervalCountView;
    private TextView intervalNextActionView;

    private String intervalCount = "";
    private String nextAction = "";
    private String actionComment = "";
    private ImageView exerciseView;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_interval, null);
        }
        intervalStartView = (TextView)mView.findViewById(R.id.interval_start_view);
        intervalCountView = (TextView)mView.findViewById(R.id.interval_count_view);
        intervalCountView.setText(intervalCount);
        intervalNextActionView = (TextView)mView.findViewById(R.id.prepare_action);
        intervalNextActionView.setText(nextAction+"/"+actionComment);
        exerciseView = (ImageView)mView.findViewById(R.id.exerceise_view);
        if(nextAction.equals("Push Up"))
            exerciseView.setImageResource(R.drawable.pushup);
        else if(nextAction.equals("Crunch"))
            exerciseView.setImageResource(R.drawable.crunch);
        else if(nextAction.equals("Squart"))
            exerciseView.setImageResource(R.drawable.squart);
        else if(nextAction.equals("Jumping Jack"))
            exerciseView.setImageResource(R.drawable.jumpjack);
        else if(nextAction.equals("Dips"))
            exerciseView.setImageResource(R.drawable.dips);
        else if(nextAction.equals("High Kniess Running"))
            exerciseView.setImageResource(R.drawable.highknessrunning);
        else if(nextAction.equals("Lunges"))
            exerciseView.setImageResource(R.drawable.lungues);
        else if(nextAction.equals("Burpees"))
            exerciseView.setImageResource(R.drawable.burpees);
        else if(nextAction.equals("Step On Chair"))
            exerciseView.setImageResource(R.drawable.steponchair);
        else if(nextAction.equals("PushUp Rotation"))
            exerciseView.setImageResource(R.drawable.pushuprotation);

        return mView;
    }

    public void setIntervalCountView(String s){
        intervalCount = s;
    }
    public void setIntervalNextAction(String s){nextAction = s;}
    public void setIntervalActionComment(String s){actionComment = s;}



}
