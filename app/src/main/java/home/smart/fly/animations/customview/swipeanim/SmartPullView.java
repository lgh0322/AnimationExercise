package home.smart.fly.animations.customview.swipeanim;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import home.smart.fly.animations.R;

public class SmartPullView extends LinearLayout {

    private static final String TAG = "SmartPullView";
    // refresh states
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    // pull state
    private static final int PULL_UP_STATE = 0;
    private static final int PULL_DOWN_STATE = 1;
    /**
     * last y
     */
    private int mLastMotionY;
    /**
     * header view
     */
    private View mHeaderView;
    private ImageView upImg;
    private TextView upText;
    /**
     * footer view
     */
    private View mFooterView;
    private ImageView downImg;
    private TextView downText;
    /**
     * scrollview
     */
    private ScrollView mScrollView;
    /**
     * header view height
     */
    private int mHeaderViewHeight;
    /**
     * footer view height
     */
    private int mFooterViewHeight;

    /**
     * layout inflater
     */
    private LayoutInflater mInflater;
    /**
     * header view current state
     */
    private int mHeaderState;
    /**
     * footer view current state
     */
    private int mFooterState;
    /**
     * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
     */
    private int mPullState;

    /**
     * footer refresh listener
     */
    private OnFooterRefreshListener mOnFooterRefreshListener;
    /**
     * footer refresh listener
     */
    private OnHeaderRefreshListener mOnHeaderRefreshListener;

    private OnPullListener mPullListener;


    /**
     * last update time
     */
    // private String mLastUpdateTime;
    public SmartPullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmartPullView(Context context) {
        super(context);
        init();
    }


    private void init() {
        // Load all of the animations we need in code rather than through XML


        mInflater = LayoutInflater.from(getContext());
        // header view ????????????,???????????????????????????linearlayout????????????
        addHeaderView();
    }

    private void addHeaderView() {
        // header view
        mHeaderView = mInflater
                .inflate(R.layout.pull_header, this, false);
        upImg = (ImageView) mHeaderView.findViewById(R.id.upImg);
        upText = (TextView) mHeaderView.findViewById(R.id.upText);
        measureView(mHeaderView);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                mHeaderViewHeight);
        // ??????topMargin???????????????header View??????,???????????????????????????
        params.topMargin = -(mHeaderViewHeight);
        setHeadViewAlpha(0);
        addView(mHeaderView, params);

    }

    private void addFooterView() {
        // footer view
        mFooterView = mInflater.inflate(R.layout.pull_footer, this, false);
        downImg = (ImageView) mFooterView.findViewById(R.id.downImg);
        downText = (TextView) mFooterView.findViewById(R.id.downText);
        // footer layout
        measureView(mFooterView);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                mFooterViewHeight);
        // int top = getHeight();
        // params.topMargin
        // =getHeight();//?????????getHeight()==0,??????onInterceptTouchEvent()?????????getHeight()???????????????,?????????0;
        // getHeight()?????????????????????,?????????????????????
        // ???????????????????????????????????????,??????AdapterView????????????MATCH_PARENT,??????footer view????????????????????????,?????????
        setFootViewAlpha(0);
        addView(mFooterView, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // footer view ???????????????????????????linearlayout????????????
        addFooterView();
        initContentAdapterView();
    }

    private void initContentAdapterView() {
        int count = getChildCount();
        if (count < 3) {
            throw new IllegalArgumentException(
                    "this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
        }
        View view = null;
        for (int i = 0; i < count - 1; ++i) {
            view = getChildAt(i);
            if (view instanceof ScrollView) {
                // finish later
                mScrollView = (ScrollView) view;
            }
        }
        if (mScrollView == null) {
            throw new IllegalArgumentException(
                    "must contain a AdapterView or ScrollView in this layout!");
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // ????????????down??????,??????y??????
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 ???????????????,< 0???????????????
                int deltaY = y - mLastMotionY;
                if (isRefreshViewScroll(deltaY)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /*
     * ?????????onInterceptTouchEvent()?????????????????????(???onInterceptTouchEvent()????????? return
     * false)??????PullToRefreshView ??????View?????????;?????????????????????????????????(??????PullToRefreshView???????????????)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // onInterceptTouchEvent????????????
                // mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:

                if (mPullListener != null) {
                    mPullListener.pull();
                }

                int deltaY = y - mLastMotionY;
                if (mPullState == PULL_DOWN_STATE) {
                    // PullToRefreshView????????????
                    Log.i(TAG, " pull down!parent view move!");
                    headerPrepareToRefresh(deltaY);
                    // setHeaderPadding(-mHeaderViewHeight);
                } else if (mPullState == PULL_UP_STATE) {
                    // PullToRefreshView????????????
                    Log.i(TAG, "pull up!parent view move!");
                    footerPrepareToRefresh(deltaY);
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:


                int topMargin = getHeaderTopMargin();
                if (mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        // ????????????
                        headerRefreshing();
                    } else {
                        // ????????????????????????????????????
                        setHeaderTopMargin(-mHeaderViewHeight);
                        setHeadViewAlpha(0);
                        if (mPullListener != null) {
                            mPullListener.pullDone();
                        }
                    }
                } else if (mPullState == PULL_UP_STATE) {
                    if (Math.abs(topMargin) >= mHeaderViewHeight
                            + mFooterViewHeight) {
                        // ????????????footer ??????
                        footerRefreshing();
                    } else {
                        // ????????????????????????????????????
                        setHeaderTopMargin(-mHeaderViewHeight);
                        setFootViewAlpha(0);
                        if (mPullListener != null) {
                            mPullListener.pullDone();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * ?????????????????????View,???PullToRefreshView??????
     *
     * @param deltaY , deltaY > 0 ???????????????,< 0???????????????
     * @return
     */
    private boolean isRefreshViewScroll(int deltaY) {
        if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
            return false;
        }

        // ??????ScrollView
        if (mScrollView != null) {
            // ???scroll view??????????????????
            View child = mScrollView.getChildAt(0);
            if (deltaY > 0 && mScrollView.getScrollY() == 0) {
                mPullState = PULL_DOWN_STATE;
                return true;
            } else if (deltaY < 0
                    && child.getMeasuredHeight() <= getHeight()
                    + mScrollView.getScrollY()) {
                mPullState = PULL_UP_STATE;
                return true;
            }
        }
        return false;
    }

    /**
     * header ????????????,??????????????????,???????????????
     *
     * @param deltaY ,?????????????????????
     */
    private void headerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // ???header view???topMargin>=0???????????????????????????????????????,??????header view ???????????????
        if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {

            mHeaderState = RELEASE_TO_REFRESH;
        } else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// ?????????????????????

            mHeaderState = PULL_TO_REFRESH;
        }
    }

    /**
     * footer ????????????,??????????????????,??????????????? ??????footer view?????????????????????header view
     * ????????????????????????????????????header view???topmargin???????????????
     *
     * @param deltaY ,?????????????????????
     */
    private void footerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // ??????header view topMargin ???????????????????????????header + footer ?????????
        // ??????footer view ??????????????????????????????footer view ???????????????
        if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
                && mFooterState != RELEASE_TO_REFRESH) {

            mFooterState = RELEASE_TO_REFRESH;
        } else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {

            mFooterState = PULL_TO_REFRESH;
        }
    }

    /**
     * ??????Header view top margin??????
     *
     * @param deltaY
     * @description
     */
    private int changingHeaderViewTopMargin(int deltaY) {
        Log.e(TAG, "changingHeaderViewTopMargin: " + deltaY);
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        float newTopMargin = params.topMargin + deltaY * 0.3f;
        // ??????????????????????????????,??????????????????????????????????????????????????????,??????????????????????????????,????????????yufengzungzhe?????????
        // ???????????????????????????????????????,??????????????????
        if (deltaY > 0 && mPullState == PULL_UP_STATE
                && Math.abs(params.topMargin) <= mHeaderViewHeight) {


            return params.topMargin;
        }
        // ?????????,????????????????????????,???????????????????????????????????????bug
        if (deltaY < 0 && mPullState == PULL_DOWN_STATE
                && Math.abs(params.topMargin) >= mHeaderViewHeight) {


            return params.topMargin;
        }
        params.topMargin = (int) newTopMargin;
        mHeaderView.setLayoutParams(params);


        if (mPullState == PULL_DOWN_STATE) {
            deltaY = deltaY > 50 ? 50 : deltaY;
            float alpah = Math.abs(1.0f - deltaY / 50.0f);
            Log.e(TAG, "changingHeaderViewTopMargin: the alpah is  " + alpah);
            setHeadViewAlpha(alpah);
        }

        if (mPullState == PULL_UP_STATE) {
            deltaY = Math.abs(deltaY);
            deltaY = deltaY > 60 ? 60 : deltaY;
            float alpah = Math.abs(1.0f - deltaY / 60.0f);
            Log.e(TAG, "changingHeaderViewTopMargin: the alpah is  " + alpah);
            setFootViewAlpha(alpah);
        }

        invalidate();
        return params.topMargin;
    }


    private void headerRefreshing() {
        mHeaderState = REFRESHING;
        setHeaderTopMargin(0);
        if (mOnHeaderRefreshListener != null) {
            mOnHeaderRefreshListener.onHeaderRefresh(this);
        }

    }

    private void footerRefreshing() {
        mFooterState = REFRESHING;
        int top = mHeaderViewHeight + mFooterViewHeight;
        setHeaderTopMargin(-top);
        setFootViewAlpha(0);
        if (mOnFooterRefreshListener != null) {
            mOnFooterRefreshListener.onFooterRefresh(this);
        }
    }

    /**
     * ??????header view ???topMargin??????
     *
     * @param topMargin ??????0????????????header view ??????????????????????????? ???-mHeaderViewHeight???????????????????????????
     *                  hylin 2012-7-31??????11:24:06
     * @description
     */
    private void setHeaderTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);


        invalidate();
    }

    public void onHeaderRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);
        setHeadViewAlpha(0);
        mHeaderState = PULL_TO_REFRESH;
    }

    /**
     * Resets the list to a normal state after a refresh.
     *
     * @param lastUpdated Last updated at.
     */
    public void onHeaderRefreshComplete(CharSequence lastUpdated) {
        // setLastUpdated(lastUpdated);
        onHeaderRefreshComplete();
    }

    /**
     * footer view ?????????????????????????????????
     */
    public void onFooterRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);
        setFootViewAlpha(0);
        mFooterState = PULL_TO_REFRESH;
    }


    /**
     * ????????????header view ???topMargin
     *
     * @return hylin 2012-7-31??????11:22:50
     * @description
     */
    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        return params.topMargin;
    }


    /**
     * set headerRefreshListener
     *
     * @param headerRefreshListener hylin 2012-7-31??????11:43:58
     * @description
     */
    public void setOnHeaderRefreshListener(
            OnHeaderRefreshListener headerRefreshListener) {
        mOnHeaderRefreshListener = headerRefreshListener;
    }

    public void setOnFooterRefreshListener(
            OnFooterRefreshListener footerRefreshListener) {
        mOnFooterRefreshListener = footerRefreshListener;
    }

    public void setOnPullListener(OnPullListener pullListener) {
        mPullListener = pullListener;
    }

    /**
     * Interface definition for a callback to be invoked when list/grid footer
     * view should be refreshed.
     */
    public interface OnFooterRefreshListener {
        void onFooterRefresh(SmartPullView view);
    }

    /**
     * Interface definition for a callback to be invoked when list/grid header
     * view should be refreshed.
     */
    public interface OnHeaderRefreshListener {
        void onHeaderRefresh(SmartPullView view);
    }

    /**
     * ????????????????????????????????????
     */
    public interface OnPullListener {
        /**
         * ???????????????????????????
         */
        void pull();

        /**
         * ???????????????????????????????????????
          */
        void pullDone();
    }


    private void setHeadViewAlpha(float alpha) {
        upImg.setAlpha(alpha);
        upText.setAlpha(alpha);
    }

    private void setFootViewAlpha(float alpha) {
        downImg.setAlpha(alpha);
        downText.setAlpha(alpha);
    }
}
