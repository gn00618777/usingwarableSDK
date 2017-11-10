package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.Manifest;
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
import android.widget.Toast;
import cwm.wearablesdk.TabataTask;

import java.util.LinkedList;
import java.util.Queue;

import cwm.wearablesdk.TabataSettings;

public class TabataFragment extends Fragment {

    private View mView;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;
    private TextView textViewActionTime;
    private TextView textViewCycke;
    private Button selectItems;
    private Button addItem;
    private Button scheduleButton;
    private Button removeItem;
    private Button startButton;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private ArrayAdapter<String> adapter3;
    private ArrayAdapter<String> adapter4;
    private ArrayAdapter<String> adapter5;
    private final String[] prepareTime = {"5 sec","10 sec","15 sec","20 sec","25 sec", "30 sec"};
    private final String[] actionType = {"count down", "count up times"};
    private final String[] intervalTime = {"10 sec", "15 sec", "20 sec", "1 min", "3 min", "5 min"};
    private final String[] actionTime = {"10 sec","15 sec","30 sec","1 min","10 min", "30 min"};
    private final String[] actionTimes = {"5","10", "15","30","40","60"};
    private final String[] cycleTimes = {"1","2","5","10","50"};
    private final CharSequence[] items = {"PUSH_UP", "CRUNCH", "SQUART", "JUMPING_JACK",
            "DIPS","HIGH_KNESSRUNNING", "LUNGES", "BURPEES", "STEP_ON_CHAIR", "PUSHUP_ROTATION"};
    private final boolean[] itemSelected = {false,false,false,false,false,false,false,false,false,false};
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
        textViewActionTime = (TextView)mView.findViewById(R.id.action_time_text);

        spinner1 = (Spinner) mView.findViewById(R.id.spinner_prepare);
        adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,prepareTime);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(parent.getSelectedItem().toString().equals("5sec")){
                    tatataSettings.setPrepareTime(5);
                }
                else if(parent.getSelectedItem().toString().equals("10 sec")){
                    tatataSettings.setPrepareTime(10);
                }
                else if(parent.getSelectedItem().toString().equals("15 sec")){
                    tatataSettings.setPrepareTime(15);
                }
                else if(parent.getSelectedItem().toString().equals("20 sec")){
                    tatataSettings.setPrepareTime(20);
                }
                else if(parent.getSelectedItem().toString().equals("25 sec")){
                    tatataSettings.setPrepareTime(25);
                }
                else if(parent.getSelectedItem().toString().equals("30 sec")){
                    tatataSettings.setPrepareTime(30);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Log.d("bernie","onNothingSelected spinner1");
            }
        });
        spinner1.setSelection(0);


        spinner2 = (Spinner) mView.findViewById(R.id.spinner_action_type);
        adapter2 = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,actionType);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                 String item = "count up times";

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if(parent.getSelectedItem().toString().equals("count down")) {
                            tatataSettings.setActionType(0);
                            textViewActionTime.setText("Action Time");
                            spinner4 = (Spinner) mView.findViewById(R.id.spinner_action_time);
                            adapter4 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,actionTime);
                            spinner4.setAdapter(adapter4);
                            //tatataSettings.setActionType(TabataSettings.COUNT_DOWN);
                            spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(parent.getSelectedItem().toString().equals("10 sec")) {
                                        tatataSettings.setActionTime(10);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("15 sec")){
                                        tatataSettings.setActionTime(15);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("30 sec")){
                                        tatataSettings.setActionTime(30);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("1 min")){
                                        tatataSettings.setActionTime(1*60);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("10 min")){
                                        tatataSettings.setActionTime(10*60);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("30 min")){
                                        tatataSettings.setActionTime(30*60);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                        else if(parent.getSelectedItem().toString().equals("count up times")){
                            tatataSettings.setActionType(1);
                            textViewActionTime.setText("Action Times");
                            spinner4 = (Spinner) mView.findViewById(R.id.spinner_action_time);
                            adapter4 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,actionTimes);
                            spinner4.setAdapter(adapter4);
                            spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    if(parent.getSelectedItem().toString().equals("5")) {
                                        tatataSettings.setActionTimes(5);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("10")){
                                        tatataSettings.setActionTimes(10);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("15")){
                                        tatataSettings.setActionTimes(15);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("30")){
                                        tatataSettings.setActionTimes(30);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("40")){
                                        tatataSettings.setActionTimes(40);
                                    }
                                    else if(parent.getSelectedItem().toString().equals("60")){
                                        tatataSettings.setActionTimes(60);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {
                                    Log.d("bernie","onNothingSelected spinner4");
                                }
                            });

                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                        Log.d("bernie","onNothingSelected spinner2");
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Log.d("bernie","onNothingSelected");
            }
        });

        spinner3 = (Spinner) mView.findViewById(R.id.spinner_interval);
        adapter3 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,intervalTime);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().toString().equals("10 sec")) {
                    tatataSettings.setIntervalTime(10);
                }
                else if(parent.getSelectedItem().toString().equals("15 sec")){
                    tatataSettings.setIntervalTime(15);
                }
                else if(parent.getSelectedItem().toString().equals("30 sec")){
                    tatataSettings.setIntervalTime(30);
                }
                else if(parent.getSelectedItem().toString().equals("1 min")){
                    tatataSettings.setIntervalTime(1*60);
                }
                else if(parent.getSelectedItem().toString().equals("3 min")){
                    tatataSettings.setIntervalTime(3*60);
                }
                else if(parent.getSelectedItem().toString().equals("5 min")){
                    tatataSettings.setActionTime(5*60);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner5 = (Spinner)mView.findViewById(R.id.spinner_cycle);
        adapter5 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,cycleTimes);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().toString().equals("1")){
                    tatataSettings.setCycle(1);
                }
                else if(parent.getSelectedItem().toString().equals("2")){
                    tatataSettings.setCycle(2);
                }
                else if(parent.getSelectedItem().toString().equals("5")){
                    tatataSettings.setCycle(5);
                }
                else if(parent.getSelectedItem().toString().equals("10")){
                    tatataSettings.setCycle(10);
                }
                else if(parent.getSelectedItem().toString().equals("50")){
                    tatataSettings.setCycle(50);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectItems = (Button)mView.findViewById(R.id.execise_items);
        selectItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Exercise Menu");
                builder.setMultiChoiceItems(items, itemSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        itemSelected[which] = isChecked;
                        itemSelected1[which+1] = isChecked;

                    }
                });
               // Log.d("bernie","itemSelected[0] is "+Boolean.toString(itemSelected[0]));
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                for(int i = 1; i < 11. ; i++){
                                    if(itemSelected1[i] == true) {
                                        tatataSettings.enableItem(i);
                                    }
                                    else
                                        tatataSettings.disableItem(i);
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        addItem = (Button) mView.findViewById(R.id.add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusCheck()){
                    TabataTask tabataTask = new TabataTask();
                    //*********************create a new setttings**********************/
                    TabataSettings tempTabataSettings = new TabataSettings();

                    for(int i = 1; i < 11. ; i++){
                        if(itemSelected1[i] == true) {
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

                    Log.d("bernie","task name:"+tabataTask.getTabataSettings().getItemName());

                    String name = tabataTask.getTabataSettings().getItemName();;
                    String prepare = Integer.toString(tabataTask.getTabataSettings().getPrepareTime());;
                    String interval = Integer.toString(tabataTask.getTabataSettings().getIntervalTime());;
                    String actionTimes = Integer.toString(tabataTask.getTabataSettings().getActionTimes());
                    scheduler.append(name+" P: "+prepare+" R: "+interval+" T:"+actionTimes+"\n");
                    mTabataQ.add(tabataTask);
                    Toast.makeText(getContext(), "Add One Task", Toast.LENGTH_SHORT).show();
                }

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
                            scheduler.append(name + " P: " + prepare + " R: " + interval + " T:" + actionTimes + "\n");
                            tempTabataQ.add(tempTask);
                        }
                    }
                    mTabataQ = new LinkedList<>();
                    mTabataQ = tempTabataQ;
                    Toast.makeText(getContext(), "Remove One Task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        scheduleButton = (Button)mView.findViewById(R.id.schedule);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Schedule");
                builder.setMessage(scheduler.toString());
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
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



}
