package home.smart.fly.animations.ui.activity.jianshu;

import android.graphics.Color;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.ColorAnimator;


public class JianShuHeadActivity extends AppCompatActivity {
    private static final String TAG = "JianShuHeadActivity";
    @BindView(R.id.searchShell)
    LinearLayout mSearchShell;
    @BindView(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.image)
    ImageView mImage;


    @BindView(R.id.head)
    RelativeLayout mHead;
    int lastColor = Color.TRANSPARENT;
    @BindView(R.id.searchTv)
    TextView mSearchTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jian_shu_head);
        ButterKnife.bind(this);
        MIUISetStatusBarLightMode(getWindow(), true);
//        StatusBarUtil.setColor(this, Color.BLACK, 0);

        final ColorAnimator mColorAnimator = new ColorAnimator(Color.TRANSPARENT, Color.WHITE);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float fraction = (float) 2 * scrollY / (mImage.getHeight());
                int color = mColorAnimator.getFractionColor(fraction);
                if (color != lastColor) {
                    lastColor = color;
                    mHead.setBackgroundColor(color);
//                    StatusBarUtil.addStatusBar(JianShuHeadActivity.this, color, 0);
                }

                Log.e(TAG, "onScrollChange: fraction=" + fraction);

                if (fraction >= 0.7f) {
                    mSearchTv.setText("??????????????????????????????");
                    ViewGroup.LayoutParams mParams = mSearchShell.getLayoutParams();
                    mParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
                    mSearchShell.setLayoutParams(mParams);
                    TransitionManager.beginDelayedTransition(mSearchShell);
                }

                if (fraction <= 0.3f) {
                    mSearchTv.setText("??????");
                    ViewGroup.LayoutParams mParams = mSearchShell.getLayoutParams();
                    mParams.width = RecyclerView.LayoutParams.WRAP_CONTENT;
                    mSearchShell.setLayoutParams(mParams);
                    TransitionManager.beginDelayedTransition(mSearchShell);
                }
            }
        });

    }

    /**
     * ?????????????????????????????????????????????MIUIV6??????
     *
     * @param window ?????????????????????
     * @param dark   ??????????????????????????????????????????????????????
     * @return boolean ??????????????????true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//??????????????????????????????
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//??????????????????
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

}
