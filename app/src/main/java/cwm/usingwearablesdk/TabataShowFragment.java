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
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import cwm.wearablesdk.TabataSettings;

public class TabataShowFragment extends Fragment {

    private View mView;
    private TextView statusView;
    private TextView itemView;
    private TextView countView;
    private TextView caloriesView;
    private TextView heartBeatView;
    private TextView historyView;
    private TextView cycleView;
    private TextView itemsView;
   // private TextView strengthView;

    private Button actionItemEndButton;
    private Button tabataDoneButton;
    private Button tabataPauseButton;
    private Button tabataResumeButton;

    private int cycle = 0;
    private int currentCycle = 0;
    private int currentDoneItm = 0;
    private int totalItems = 0;

    private boolean hideActionItemEndButton = true;

    String status = "";
    String item = "";
    String count = "";
    String calories = "";
    String heartBeat = "";
    String history = "";

    String actionItemEnd = "";
    //String strength = null;

    ListenForTabataShowFragment mCallback;

    public interface ListenForTabataShowFragment {
        public void onPressActionItemEndButton();
        public void onPressTabataDoneButton();
        public void onPressTabataPauseButton();
        public void onPressTabataResumeButton();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
                mCallback = (ListenForTabataShowFragment) context;
        }catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement ListenForSwVersionFragment");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_show, null);
        }
        cycleView = (TextView)mView.findViewById(R.id.cycle_view);
        cycleView.setText("cycle: "+Integer.toString(currentCycle)+"/"+Integer.toString(cycle));
        itemsView = (TextView)mView.findViewById(R.id.items_view);
        itemsView.setText("items: "+Integer.toString(currentDoneItm)+"/"+Integer.toString(totalItems) );
        statusView = (TextView) mView.findViewById(R.id.status);
        itemView = (TextView) mView.findViewById(R.id.exercise_item);
        countView = (TextView) mView.findViewById(R.id.action_count);
        caloriesView = (TextView) mView.findViewById(R.id.action_calories);
        heartBeatView = (TextView) mView.findViewById(R.id.action_heart);
        historyView = (TextView) mView.findViewById(R.id.histroy);

        tabataPauseButton = (Button)mView.findViewById(R.id.tabata_pause);
        tabataPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressTabataPauseButton();
            }
        });
        actionItemEndButton = (Button)mView.findViewById(R.id.action_item_end);
        actionItemEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressActionItemEndButton();
            }
        });

        if(hideActionItemEndButton == false){
            actionItemEndButton.setVisibility(View.VISIBLE);
        }
        else{
            actionItemEndButton.setVisibility(View.GONE);
        }


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


        historyView.setText(history);
        statusView.setText("Status:  "+status);
        itemView.setText("Action Item:  "+item);
        countView.setText("Action Count:  "+count);
        caloriesView.setText("Action Calories:  "+calories);
        heartBeatView.setText("Heart Beat:  "+heartBeat);
        return mView;
    }

    public void setTabataResultValue(String status, String item, String count, String calories, String heartBeat/*, String strength*/)
    {
        this.status = status;
        this.item = item;
        this.count = count;
        this.calories = calories;
        this.heartBeat = heartBeat;
        //this.strength = strength;
    }
    public void setHistory(String builder)
    {
        history = builder;
    }

    public void setActionItemEndView(String s){
        actionItemEnd = s;
    }

    public void setCycles(int currentCycle, int totalCycle){
        this.currentCycle = currentCycle;
        this.cycle = totalCycle;
    }
    public void hideButton(boolean b){
        hideActionItemEndButton = b;
    }

    public void setItems(int currentDone, int totalItems){
        currentDoneItm = currentDone;
        this.totalItems = totalItems;
    }
    public void setCycleView(int s, int s1){
        currentCycle = s;
        cycle = s1;
    }


}
