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

public class TabataShowFragment extends Fragment {

    private View mView;
    private TextView statusView;
    private TextView itemView;
    private TextView countView;
    private TextView caloriesView;
    private TextView strengthView;
    private TextView historyView;
    private TextView itemsView;

    private Button doneButton;

    private int currentDoneItm = 0;
    private int totalItems = 0;

    private boolean hideActionItemEndButton = true;

    String status = "";
    String item = "";
    String count = "";
    String calories = "";
    String history = "";
    String strength = "";

    ListenForTabataShowFragment mCallback;

    public interface ListenForTabataShowFragment {
        public void onPressTabataDoneButton();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
                mCallback = (ListenForTabataShowFragment) context;
        }catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement ListenForShowFragment");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_show, null);
        }
        itemsView = (TextView)mView.findViewById(R.id.items_view);
        itemsView.setText("items: "+Integer.toString(currentDoneItm)+"/"+Integer.toString(totalItems) );
        statusView = (TextView) mView.findViewById(R.id.status);
        itemView = (TextView) mView.findViewById(R.id.exercise_item);
        countView = (TextView) mView.findViewById(R.id.action_count);
        caloriesView = (TextView) mView.findViewById(R.id.action_calories);
        strengthView = (TextView) mView.findViewById(R.id.action_strength);
        historyView = (TextView) mView.findViewById(R.id.histroy);
        doneButton = (Button)mView.findViewById(R.id.tabata_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressTabataDoneButton();
            }
        });

        historyView.setText(history);
        statusView.setText("Status:  "+status);
        itemView.setText("Action Item:  "+item);
        countView.setText("Action Count:  "+count);
        caloriesView.setText("Action Calories:  "+calories);
        strengthView.setText("Action Strength:  "+strength);
        return mView;
    }

    public void setTabataResultValue(String status, String item, String count, String calories, String strength)
    {
        this.status = status;
        this.item = item;
        this.count = count;
        this.calories = calories;
        this.strength = strength;
    }
    public void setHistory(String builder)
    {
        history = builder;
    }

    public void setItems(int currentDone, int totalItems){
        currentDoneItm = currentDone;
        this.totalItems = totalItems;
    }

}
