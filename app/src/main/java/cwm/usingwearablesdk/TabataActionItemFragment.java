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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import cwm.wearablesdk.TabataSettings;

public class TabataActionItemFragment extends Fragment {

    private View mView;


    private TextView actionItemView;
    private TextView actionItemStartView;
    private TextView actionCountView;

   // private ImageView exerciseView;

    private String actionItem="";
    private String actionItemStart="";
    private String actionComment="";

    private String count = "";


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_action_item, null);
        }

        actionItemView = (TextView)mView.findViewById(R.id.action_item_view);
        actionItemView.setText(actionItem+"/"+actionComment);
        actionItemStartView = (TextView)mView.findViewById(R.id.action_item_start_view);
        actionItemStartView.setText(actionItemStart);
        //exerciseView = (ImageView)mView.findViewById(R.id.exerceise_view);
        actionCountView = (TextView)mView.findViewById(R.id.action_item_count);
        actionCountView.setText(count);
        //exerciseView.setImageResource(R.drawable.pushup);

       /* if(actionItem.equals("Push Up"))
            exerciseView.setImageResource(R.drawable.pushup);
        else if(actionItem.equals("Crunch"))
            exerciseView.setImageResource(R.drawable.crunch);
        else if(actionItem.equals("Squart"))
            exerciseView.setImageResource(R.drawable.squart);
        else if(actionItem.equals("Jumping Jack"))
            exerciseView.setImageResource(R.drawable.jumpjack);
        else if(actionItem.equals("Dips"))
            exerciseView.setImageResource(R.drawable.dips);
        else if(actionItem.equals("High Kniess Running"))
            exerciseView.setImageResource(R.drawable.highknessrunning);
        else if(actionItem.equals("Lunges"))
            exerciseView.setImageResource(R.drawable.lungues);
        else if(actionItem.equals("Burpees"))
            exerciseView.setImageResource(R.drawable.burpees);
        else if(actionItem.equals("Step On Chair"))
            exerciseView.setImageResource(R.drawable.steponchair);
        else if(actionItem.equals("PushUp Rotation"))
            exerciseView.setImageResource(R.drawable.pushuprotation);*/

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

}
