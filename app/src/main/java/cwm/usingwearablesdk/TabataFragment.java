package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cwm.wearablesdk.TabataSettings;

public class TabataFragment extends Fragment {

    private View mView;

    private TextView scheduleList;

    /*********************************/
    private Button prepareButton;
    private Button intervalButton;
    private Button actionTimesButton;
    private Button typeButton;
    private Button exerciseItemsButton;
    private Button startButton;
    private Button cycleButton;
    /*********************************/

    private List<String> exerciseList;
    private ListView listView;


    //for menu list
    private ListAdapter listAdapter;
    private List<String> stringList = new ArrayList<>();
    private ListView.OnItemClickListener listViewOnItemClickListener;
    private MyDBHelper myDBHelper;

    private RadioGroup radioGroupPrepare;
    private RadioGroup radioGroupInterval;
    private RadioGroup radioGroupTimes;
    private RadioGroup radioGroupType;
    private RadioButton radioButtonPrepare;
    private RadioButton radioButtonInterval;
    private RadioButton radioButtonTimes;
    private RadioButton radioButtonType;

    private int prepare = 5;
    private int interval = 10;
    private int times = 5;
    private int type = 1;
    private int cycle = 1;

    private int index = 0;


    private final CharSequence[] items = {"PUSH_UP", "CRUNCH", "SQUART", "JUMPING_JACK",
            "DIPS","HIGH_KNESSRUNNING", "LUNGES", "BURPEES", "STEP_ON_CHAIR", "PUSHUP_ROTATION"};
    private final boolean[] itemSelected1 = {false,false,false,false,false,false,false,false,false,false,false};

    private TabataSettings tatataSettings = new TabataSettings();

    private Queue<TabataTask> mTabataQ = new LinkedList<>();
    Queue<TabataTask> tempTabataQ = new LinkedList<>();
    private StringBuilder scheduler = new StringBuilder();
    private StringBuilder itemSchedule = new StringBuilder();
    private StringBuilder parameterSchedule = new StringBuilder();

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
        myDBHelper = new MyDBHelper(getContext(), Environment.getExternalStorageDirectory().toString() + "/Download/"+"tabatamenu.db", null, 1);
        scheduler = new StringBuilder();
        scheduleList = (TextView)mView.findViewById(R.id.schedule_list);
        scheduleList.setText("");

        prepareButton = (Button)mView.findViewById(R.id.prepare_time);
        prepareButton.setText("預備時間\n"+Integer.toString(prepare));
        intervalButton = (Button)mView.findViewById(R.id.interval_time);
        intervalButton.setText("間隔時間\n"+Integer.toString(interval));
        cycleButton = (Button)mView.findViewById(R.id.cycle);
        cycleButton.setText("循環次數\n"+Integer.toString(cycle));
        actionTimesButton = (Button)mView.findViewById(R.id.action_times);
        actionTimesButton.setText("運動次數\n"+Integer.toString(times));
        typeButton = (Button)mView.findViewById(R.id.action_type);
        typeButton.setText("達標類型\n"+Integer.toString(type));
        exerciseItemsButton = (Button)mView.findViewById(R.id.exercise_item);
        startButton = (Button)mView.findViewById(R.id.start_button);



        prepareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepare = 5;
                prepareButton.setText("預備時間\n"+Integer.toString(prepare));
                final View configs = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_prepare, null);
                radioGroupPrepare = (RadioGroup)configs.findViewById(R.id.radio_group1);
                radioButtonPrepare = (RadioButton)configs.findViewById(R.id.prepare_one);
                radioButtonPrepare.setChecked(true);

                radioGroupPrepare.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if(checkedId == R.id.prepare_one){
                            prepare = 5;
                            prepareButton.setText("預備時間\n"+Integer.toString(prepare));
                        }
                        else if(checkedId == R.id.preapre_two){
                            prepare = 10;
                            prepareButton.setText("預備時間\n"+Integer.toString(prepare));
                        }
                        else if(checkedId == R.id.prepare_three){
                            prepare = 15;
                            prepareButton.setText("預備時間\n"+Integer.toString(prepare));
                        }
                        else if(checkedId == R.id.prepare_four){
                            prepare = 20;
                            prepareButton.setText("預備時間\n"+Integer.toString(prepare));
                        }
                    }
                });

                new AlertDialog.Builder(getContext())
                        .setTitle("Prepare")
                        .setView(configs)
                        .setPositiveButton(R.string.ok,null)
                        .show();

            }
        });

        intervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interval = 10;
                intervalButton.setText("間隔時間\n"+Integer.toString(interval));
                final View configs = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_interval, null);
                radioGroupInterval = (RadioGroup)configs.findViewById(R.id.radio_group2);
                radioButtonInterval = (RadioButton)configs.findViewById(R.id.interval_one);
                radioButtonInterval.setChecked(true);

                radioGroupInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if(checkedId == R.id.interval_one){
                            interval = 10;
                            intervalButton.setText("間隔時間\n"+Integer.toString(interval));
                        }
                        else if(checkedId == R.id.interval_two){
                            interval = 15;
                            intervalButton.setText("間隔時間\n"+Integer.toString(interval));
                        }
                        else if(checkedId == R.id.interval_three){
                            interval = 20;
                            intervalButton.setText("間隔時間\n"+Integer.toString(interval));
                        }
                        else if(checkedId == R.id.interval_four){
                            interval = 60;
                            intervalButton.setText("間隔時間\n"+Integer.toString(interval));
                        }
                    }
                });

                new AlertDialog.Builder(getContext())
                        .setTitle("Interval")
                        .setView(configs)
                        .setPositiveButton(R.string.ok,null)
                        .show();
            }
        });

        actionTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset
                times = 5;
                actionTimesButton.setText("運動次數\n"+Integer.toString(times));
                final View configs = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_times, null);
                radioGroupTimes = (RadioGroup)configs.findViewById(R.id.radio_group3);
                radioButtonTimes = (RadioButton)configs.findViewById(R.id.times_one);
                radioButtonTimes.setChecked(true);
                radioGroupTimes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if(checkedId == R.id.times_one){
                            times = 5;
                            actionTimesButton.setText("運動次數\n"+Integer.toString(times));
                        }
                        else if(checkedId == R.id.times_two){
                            times = 10;
                            actionTimesButton.setText("運動次數\n"+Integer.toString(times));
                        }
                        else if(checkedId == R.id.times_three){
                            times = 15;
                            actionTimesButton.setText("運動次數\n"+Integer.toString(times));
                        }
                        else if(checkedId == R.id.times_four){
                            times = 30;
                            actionTimesButton.setText("運動次數\n"+Integer.toString(times));
                        }
                    }
                });


                new AlertDialog.Builder(getContext())
                        .setTitle("Times")
                        .setView(configs)
                        .setPositiveButton(R.string.ok,null)
                        .show();
            }
        });

        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset
                type = 1;
                final View configs = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_type, null);
                radioGroupType = (RadioGroup)configs.findViewById(R.id.radio_group4);
                radioButtonType = (RadioButton)configs.findViewById(R.id.type_one);
                radioButtonType.setChecked(true);
                radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if(checkedId == R.id.type_one){
                            type = 0;
                        }
                        else if(checkedId == R.id.type_two){
                            type = 1;
                        }
                    }
                });

                new AlertDialog.Builder(getContext())
                        .setTitle("Type")
                        .setView(configs)
                        .setPositiveButton(R.string.ok,null)
                        .show();
            }
        });

        exerciseList = new ArrayList<String>();
        exerciseList.add("PUSH_UP/伏地挺身");
        exerciseList.add("CRUNCH/捲腹");
        exerciseList.add("SQUART/深蹲");
        exerciseList.add("JUMPING_JACK/開合跳");
        exerciseList.add("DIPS/椅子三頭肌撐體");
        exerciseList.add("HIGH_KNESSRUNNING/原地提膝踏步");
        exerciseList.add("LUNGES/前屈深蹲");
        exerciseList.add("BURPEES/波比跳");
        exerciseList.add("STEP_ON_CHAIR/登階運動");
        exerciseList.add("PUSHUP_ROTATION/T型伏地挺身");

        exerciseItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset
                for(int i = 0 ; i < 11 ; i++){
                    itemSelected1[i] = false;
                }
                mTabataQ = new LinkedList<>();

                final View configs = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_exercise_items, null);
                listView = (ListView) configs.findViewById(R.id.listView1);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CheckedTextView chkItem = (CheckedTextView) view.findViewById(R.id.check1);
                        chkItem.setChecked(!chkItem.isChecked());
                        itemSelected1[position+1] = chkItem.isChecked();
                    }
                });
                ListAdapter listAdapter = new ListAdapter(getActivity(), exerciseList);
                listView.setAdapter(listAdapter);


                new AlertDialog.Builder(getContext())
                        .setTitle("Exercise Menu")
                        .setView(configs)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i = 1 ; i < 11 ; i ++) {
                                    if(itemSelected1[i] == true) {
                                        TabataTask tabataTask = new TabataTask();
                                        TabataSettings tempTabataSettings = new TabataSettings();
                                        tempTabataSettings.enableItem(i);
                                        tempTabataSettings.setActionType(type);
                                        tempTabataSettings.setCycle(1);
                                        tempTabataSettings.setActionTimes(times);
                                        tempTabataSettings.setPrepareTime(prepare);
                                        tempTabataSettings.setIntervalTime(interval);
                                        tabataTask.setTabataSettings(tempTabataSettings);
                                        mTabataQ.add(tabataTask);
                                    }
                                }
                            }
                        })
                        .show();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusCheck()){
                    if(mTabataQ.size() != 0)
                        mCallback.onInitTabata(mTabataQ);
                }

            }
        });

        return mView;
    }

    public boolean statusCheck(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        if(mTabataQ.size() == 0) {
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

   class ListAdapter extends BaseAdapter
   {
       private Activity activity;
       private List<String> mList;

       private LayoutInflater inflater = null;

       public ListAdapter(Activity a, List<String> list)
       {
           activity = a;
           mList = list;
           inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       }

       public int getCount()
       {
           return mList.size();
       }

       public Object getItem(int position)
       {
           return position;
       }

       public long getItemId(int position)
       {
           return position;
       }

       public View getView(int position, View convertView, ViewGroup parent)
       {
           View vi = convertView;
           if(convertView==null)
           {
               vi = inflater.inflate(R.layout.list_item, null);
           }

           CheckedTextView chkBshow = (CheckedTextView) vi.findViewById(R.id.check1);

           chkBshow.setText(mList.get(position).toString());

           return vi;
       }
   }




}
