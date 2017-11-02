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

public class TabataOperateFragment extends Fragment {

    private View mView;
    private TextView cycleView;
    private TextView prepareStartView;
    private TextView prepareCountView;
    private TextView prepareEndView;
    private TextView actionItemView;
    private TextView actionItemStartView;
    private TextView actionItemEndView;
    private TextView intervalStartView;
    private TextView intervalCountView;
    private TextView intervalEndView;
    private TextView tabataDoneView;

    private Button prepareStartButton;
    private Button prepareCountButton;
    private Button prepareEndButton;
    private Button actionItemtButton;
    private Button actionItemStartButton;
    private Button actionItemEndButton;
    private Button intervalStartButton;
    private Button intervalCountButton;
    private Button intervalEndButton;
    private Button tabataDoneButton;

    private int cycle;
    private int currentCycle;

    private String prepareStart;
    private String prepareCount;
    private String prepareEnd;
    private String actionItem;
    private String actionItemStart;
    private String actionItemEnd;
    private String intervalStart;
    private String intervalCount;
    private String intervalEnd;
    private String tabataDone;

    private TextView statusView;
    private TextView itemView;
    private TextView countView;
    private TextView caloriesView;
    private TextView heartBeatView;
    private TextView historyView;

    String status = "";
    String item = "";
    String count = "";
    String calories = "";
    String heartBeat = "";
    String history = "";

    public interface ListenForTabataOperateFragment {
        public void onPressPrepareStartButton();
        public void onPressPrepareCountButton();
        public void onPressPrepareEndButton();
        public void onPressActionItemButton();
        public void onPressActionItemStartButton();
        public void onPressActionItemEndButton();
        public void onPressItervalStartButton();
        public void onPressIntervalCountButton();
        public void onPressIntervalEndButton();
        public void onPressTabataDoneButton();
    }

    ListenForTabataOperateFragment mCallback;



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForTabataOperateFragment) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForTabataOperateFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_operate, null);
        }
        cycleView = (TextView)mView.findViewById(R.id.cycle_view);
        cycleView.setText("cycle: "+Integer.toString(currentCycle)+"/"+Integer.toString(cycle));
        prepareStartView = (TextView)mView.findViewById(R.id.prepare_start_view);
        prepareStartView.setText(prepareStart);
        prepareCountView = (TextView)mView.findViewById(R.id.prepare_count_view);
        prepareCountView.setText(prepareCount);
        prepareEndView = (TextView)mView.findViewById(R.id.prepare_end_view);
        prepareEndView.setText(prepareEnd);
        actionItemView = (TextView)mView.findViewById(R.id.action_item_view);
        actionItemView.setText(actionItem);
        actionItemStartView = (TextView)mView.findViewById(R.id.action_item_start_view);
        actionItemStartView.setText(actionItemStart);
        actionItemEndView = (TextView)mView.findViewById(R.id.action_item_end_view);
        actionItemEndView.setText(actionItemEnd);
        intervalStartView = (TextView)mView.findViewById(R.id.interval_start_view);
        intervalStartView.setText(intervalStart);
        intervalCountView = (TextView)mView.findViewById(R.id.interval_count_view);
        intervalCountView.setText(intervalCount);
        intervalEndView = (TextView)mView.findViewById(R.id.interval_end_view);
        intervalEndView.setText(intervalEnd);
        tabataDoneView = (TextView)mView.findViewById(R.id.tabata_done_view);
        tabataDoneView.setText(tabataDone);

        statusView = (TextView) mView.findViewById(R.id.status);
        itemView = (TextView) mView.findViewById(R.id.exercise_item);
        countView = (TextView) mView.findViewById(R.id.action_count);
        caloriesView = (TextView) mView.findViewById(R.id.action_calories);
        heartBeatView = (TextView) mView.findViewById(R.id.action_heart);

        statusView.setText("Status:  "+status);
        itemView.setText("Action Item:  "+item);
        countView.setText("Action Count:  "+count);
        caloriesView.setText("Action Calories:  "+calories);
        heartBeatView.setText("Heart Beat:  "+heartBeat);

        prepareStartButton = (Button)mView.findViewById(R.id.prepare_start);
        prepareCountButton = (Button)mView.findViewById(R.id.prepare_count);
        prepareEndButton = (Button)mView.findViewById(R.id.prepare_end);
        actionItemtButton = (Button)mView.findViewById(R.id.action_item);
        actionItemStartButton = (Button)mView.findViewById(R.id.action_item_start);
        actionItemEndButton = (Button)mView.findViewById(R.id.action_item_end);
        intervalStartButton = (Button)mView.findViewById(R.id.interval_start);
        intervalCountButton = (Button)mView.findViewById(R.id.interval_count);
        intervalEndButton = (Button)mView.findViewById(R.id.interval_end);
        tabataDoneButton = (Button)mView.findViewById(R.id.tabata_done);

        prepareStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressPrepareStartButton();
            }
        });
        prepareCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressPrepareCountButton();
            }
        });
        prepareEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressPrepareEndButton();
            }
        });
        actionItemtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressActionItemButton();
            }
        });
        actionItemStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressActionItemStartButton();
            }
        });
        actionItemEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressActionItemEndButton();
            }
        });
        intervalStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressItervalStartButton();
            }
        });
        intervalCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressIntervalCountButton();
            }
        });
        intervalEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressIntervalEndButton();
            }
        });
        tabataDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressTabataDoneButton();
            }
        });

        return mView;
    }
    public void setCycles(int currentCycle, int totalCycle){
        this.currentCycle = currentCycle;
        this.cycle = totalCycle;
    }
    public void setPrepareStartView(String s){
        prepareStart = s;
    }
    public void setPrepareCountView(String s){
        prepareCount = s;
    }
    public void setPrepareEndView(String s){
        prepareEnd = s;
    }
    public void setActionItemView(String item){
        actionItem = item;
    }
    public void setActionItemStartView(String s){
        actionItemStart = s;
    }
    public void setActionItemEndView(String s){
        actionItemEnd = s;
    }
    public void setIntervalStartView(String s){
        intervalStart = s;
    }
    public void setIntervalCountView(String s){
        intervalCount = s;
    }
    public void setIntervalEndView(String s){
        intervalEnd = s;
    }
    public void setTabataDoneView(String s){
        tabataDone = s;
    }
    public void setTabataResultValue(String status, String item, String count, String calories, String heartBeat)
    {
        this.status = status;
        this.item = item;
        this.count = count;
        this.calories = calories;
        this.heartBeat = heartBeat;
    }
    /*public void setHistory(String builder)
    {
        history = builder;
    }*/

    public void setGone(boolean b){
        if(b == true){
            prepareStartButton.setVisibility(View.GONE);
            prepareStartView.setVisibility(View.GONE);
            prepareCountButton.setVisibility(View.GONE);
            prepareEndButton.setVisibility(View.GONE);
            prepareEndView.setVisibility(View.GONE);
        }
        else{
            prepareStartButton.setVisibility(View.VISIBLE);
            prepareStartView.setVisibility(View.VISIBLE);
            prepareCountButton.setVisibility(View.VISIBLE);
            prepareEndButton.setVisibility(View.VISIBLE);
            prepareEndView.setVisibility(View.VISIBLE);
        }

    }


}
