package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
    private TextView connectionView;
    private TextView initCodeView;

    private Button tabataPauseButton;
    private Button tabataDoneButton;
    private Button tabataResumeButton;

    private ImageView exerciseView;

    private String actionItem="";
    private String actionItemStart="";
    private String actionComment="";

    private boolean mAniStarted = false;
    private boolean mConnectState = false;
    private int initCode = 0;

    private String count = "";

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_action_item, null);
        }
        connectionView = (TextView)mView.findViewById(R.id.connect_state);
        if(mConnectState == false)
          connectionView.setText("connect status: "+"斷線");
        else
            connectionView.setText("connect status: "+"連線");
        initCodeView = (TextView)mView.findViewById(R.id.init_state);
        if(initCode == 0)
           initCodeView.setText("init status: not pass");
        else
            initCodeView.setText("init status: pass");
        actionItemView = (TextView)mView.findViewById(R.id.action_item_view);
        actionItemView.setText(actionItem+"/"+actionComment);
        actionItemStartView = (TextView)mView.findViewById(R.id.action_item_start_view);
        actionItemStartView.setText(actionItemStart);
        exerciseView = (ImageView)mView.findViewById(R.id.exerceise_view);
        actionCountView = (TextView)mView.findViewById(R.id.action_item_count);
        actionCountView.setText(count);
        if(actionItem.equals("Push Up")) {
            exerciseView.setBackgroundResource(R.drawable.anim_pushup_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("Crunch")) {
            exerciseView.setBackgroundResource(R.drawable.anim_crunch_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("Squart")) {
            exerciseView.setBackgroundResource(R.drawable.anim_squart_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("Jumping Jack")) {
            exerciseView.setBackgroundResource(R.drawable.anim_jumpjack_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("Dips")) {
            exerciseView.setBackgroundResource(R.drawable.anim_dip_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("High Kniess Running")) {
            exerciseView.setBackgroundResource(R.drawable.anim_highknessrunning_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("Lunges")) {
            exerciseView.setBackgroundResource(R.drawable.anim_lunge_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("Burpees")) {
            exerciseView.setBackgroundResource(R.drawable.anim_burpee_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("Step On Chair")) {
            exerciseView.setBackgroundResource(R.drawable.anim_steponchair_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
        }
        else if(actionItem.equals("PushUp Rotation")) {
            exerciseView.setBackgroundResource(R.drawable.anim_pushuprotation_gif);
            if(!mAniStarted) {
                mAniStarted = true;
                AnimationDrawable anim = null;
                Object ob = exerciseView.getBackground();
                anim = (AnimationDrawable) ob;
                anim.stop();
                anim.start();
            }
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
    public void setInitCode(int code){initCode = code;}
    public void setAni(boolean b){mAniStarted = b;}

}
