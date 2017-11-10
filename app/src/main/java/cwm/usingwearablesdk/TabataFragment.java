package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import cwm.wearablesdk.TabataTask;

import java.util.LinkedList;
import java.util.Queue;

import cwm.wearablesdk.TabataSettings;

public class TabataFragment extends Fragment {

    private View mView;

    private TextView scheduleList;

    private Button newAddItem;
    private Button removeItem;
    private Button startButton;

    private RadioGroup radioGroupPrepare;
    private RadioGroup radioGroupInterval;
    private RadioGroup radioGroupTimes;
    private RadioButton radioButtonPrepare;
    private RadioButton radioButtonInterval;
    private RadioButton radioButtonTimes;

    private int prepare = 5;
    private int interval = 10;
    private int times = 5;

    private int index = 0;

    private final CharSequence[] items = {"PUSH_UP", "CRUNCH", "SQUART", "JUMPING_JACK",
            "DIPS","HIGH_KNESSRUNNING", "LUNGES", "BURPEES", "STEP_ON_CHAIR", "PUSHUP_ROTATION"};
    private final boolean[] itemSelected1 = {false,false,false,false,false,false,false,false,false,false,false};

    private TabataSettings tatataSettings = new TabataSettings();

    private Queue<TabataTask> mTabataQ = new LinkedList<>();
    Queue<TabataTask> tempTabataQ = new LinkedList<>();
    private StringBuilder scheduler = new StringBuilder();

    // Container Activity must implement this interface
    public interface ListenForTabataFragment {
        public void onInitTabata(Queue<TabataTask> mTabataQ);
    }

    private ListenForTabataFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForTabataFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForPersonalInfoFragment");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata, null);
        }

        scheduler = new StringBuilder();
        scheduleList = (TextView)mView.findViewById(R.id.schedule_list);
        scheduleList.setText("");

        newAddItem = (Button)mView.findViewById(R.id.add_item_new);
        newAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Exercise Menu");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        index = which+1;
                    }
                });
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                for(int i = 1; i < 11; i++){
                                    itemSelected1[i] = false;
                                }
                                itemSelected1[index] = true;
                                tatataSettings.enableItem(index);

                                final View configs = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_config, null);
                                radioGroupPrepare = (RadioGroup)configs.findViewById(R.id.radio_group1);
                                radioGroupInterval = (RadioGroup)configs.findViewById(R.id.radio_group2);
                                radioGroupTimes = (RadioGroup)configs.findViewById(R.id.radio_group3);

                                radioButtonPrepare = (RadioButton)configs.findViewById(R.id.prepare_one);
                                radioButtonPrepare.setChecked(true);

                                radioGroupPrepare.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                                        if(checkedId == R.id.prepare_one){
                                           prepare = 5;
                                        }
                                        else if(checkedId == R.id.preapre_two){
                                            prepare = 10;
                                        }
                                        else if(checkedId == R.id.prepare_three){
                                            prepare = 15;
                                        }
                                        else if(checkedId == R.id.prepare_four){
                                            prepare = 20;
                                        }
                                    }
                                });
                                radioButtonInterval = (RadioButton)configs.findViewById(R.id.interval_one);
                                radioButtonInterval.setChecked(true);
                                radioGroupInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                                        if(checkedId == R.id.interval_one){
                                            interval = 10;
                                        }
                                        else if(checkedId == R.id.interval_two){
                                            interval = 15;
                                        }
                                        else if(checkedId == R.id.interval_three){
                                            interval = 20;
                                        }
                                        else if(checkedId == R.id.interval_four){
                                            interval = 60;
                                        }
                                    }
                                });
                                radioButtonTimes = (RadioButton)configs.findViewById(R.id.times_one);
                                radioButtonTimes.setChecked(true);
                                radioGroupTimes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                                        if(checkedId == R.id.times_one){
                                            times = 5;
                                        }
                                        else if(checkedId == R.id.times_two){
                                            times = 10;
                                        }
                                        else if(checkedId == R.id.times_three){
                                            times = 15;
                                        }
                                        else if(checkedId == R.id.times_four){
                                            times = 30;
                                        }
                                    }
                                });


                                new AlertDialog.Builder(getContext())
                                        .setTitle("Parameters")
                                        .setView(configs)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tatataSettings.setPrepareTime(prepare);
                                                tatataSettings.setIntervalTime(interval);
                                                tatataSettings.setActionType(1);
                                                tatataSettings.setActionTimes(times);
                                                tatataSettings.setCycle(1);
                                                Log.d("bernie",Integer.toString(tatataSettings.getActionTimes()));

                                                if(statusCheck()){
                                                    //*********************create a new setttings**********************/
                                                    TabataTask tabataTask = new TabataTask();
                                                    TabataSettings tempTabataSettings = new TabataSettings();
                                                    for(int i = 1; i < 11. ; i++){
                                                        if(itemSelected1[i] == true) {
                                                            Log.d("bernie","yes selected");
                                                            tempTabataSettings.enableItem(i);
                                                        }
                                                        else
                                                            tempTabataSettings.disableItem(i);
                                                    }
                                                    tempTabataSettings.setActionType(tatataSettings.getActionType());
                                                    tempTabataSettings.setCycle(tatataSettings.getCycle());
                                                    tempTabataSettings.setActionTimes(tatataSettings.getActionTimes());
                                                    tempTabataSettings.setPrepareTime(tatataSettings.getPrepareTime());
                                                    tempTabataSettings.setIntervalTime(tatataSettings.getIntervalTime());

                                                    //***************************************************************/

                                                    tabataTask.setTabataSettings(tempTabataSettings);

                                                    String name = tabataTask.getTabataSettings().getItemName();;
                                                    String prepare = Integer.toString(tabataTask.getTabataSettings().getPrepareTime());;
                                                    String interval = Integer.toString(tabataTask.getTabataSettings().getIntervalTime());;
                                                    String actionTimes = Integer.toString(tabataTask.getTabataSettings().getActionTimes());
                                                    scheduler.append(name+"→"+" Prepare: "+prepare+" Rest: "+interval+" Times:"+actionTimes+"\n");
                                                    mTabataQ.add(tabataTask);

                                                    Toast.makeText(getContext(), "Add One Task", Toast.LENGTH_SHORT).show();
                                                    scheduleList.setText(scheduler.toString());
                                                }
                                            }
                                        })
                                        .show();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        removeItem = (Button)mView.findViewById(R.id.remove_item);
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(mTabataQ.size() != 0) {
                    scheduler = new StringBuilder();
                    TabataTask tempTask = new TabataTask();
                    String name = "";
                    String prepare = "";
                    String interval = "";
                    String actionTimes = "";
                    int size = mTabataQ.size();
                    for(int i = 0 ; i < size ; i++) {
                        tempTask = mTabataQ.poll();
                        if(i < size-1) {
                            name = tempTask.getTabataSettings().getItemName();
                            prepare = Integer.toString(tempTask.getTabataSettings().getPrepareTime());
                            interval = Integer.toString(tempTask.getTabataSettings().getIntervalTime());
                            actionTimes = Integer.toString(tempTask.getTabataSettings().getActionTimes());
                            scheduler.append(name +"→"+" Prepare: " + prepare + " Rest: " + interval + " Times:" + actionTimes + "\n");
                            tempTabataQ.add(tempTask);
                        }
                    }
                    mTabataQ = new LinkedList<>();
                    mTabataQ = tempTabataQ;
                    scheduleList.setText(scheduler.toString());
                    Toast.makeText(getContext(), "Remove One Task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startButton = (Button) mView.findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTabataQ.size() != 0)
                   mCallback.onInitTabata(mTabataQ);
                scheduler = new StringBuilder();
            }
        });

        return mView;
    }

    public boolean statusCheck(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        if(tatataSettings.getPrepareTime() == 0){
            builder.setMessage("Please choise one of Prepare Time");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return false;
        }
        if(tatataSettings.getActionType() == 3){
            builder.setMessage("Please choise one of Action Type");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return false;
        }
        if(tatataSettings.getIntervalTime() == 0){
            builder.setMessage("Please choise one of Interval Time");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return false;
        }
        if(tatataSettings.getActionType() == 0){
            if(tatataSettings.getActionTime() == 0) {
                builder.setMessage("Please choise one of Action Time");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return false;
            }
        }
        if(tatataSettings.getActionType() == 1){
            if(tatataSettings.getActionTimes() == 0) {
                builder.setMessage("Please choise one of Action Times");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return false;
            }
        }
        if(tatataSettings.getCycle() == 0){
            builder.setMessage("Please choise one of Cycles");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return false;
        }
        if(tatataSettings.getTotalItemsNumber() == 0){
            builder.setMessage("Please choise one of Exercise Item");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return false;
        }
        return true;
    }
    public void setReset(){
        mTabataQ = new LinkedList<>();
        scheduler = new StringBuilder();
        scheduleList.setText("");
    }



}
