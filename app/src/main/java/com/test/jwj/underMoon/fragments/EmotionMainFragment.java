package com.test.jwj.underMoon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.test.jwj.underMoon.Callback.MsgCallback;
import com.test.jwj.underMoon.CustomView.EmotionKeyboard;
import com.test.jwj.underMoon.CustomView.NoHorizontalScrollerViewPager;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.NoHorizontalScrollerVPAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.ChatEntity;
import com.test.jwj.underMoon.bean.MessageTabEntity;
import com.test.jwj.underMoon.database.ImDB;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.utils.EmotionUtils;
import com.test.jwj.underMoon.utils.GlobalOnItemClickManagerUtils;
import com.test.jwj.underMoon.utils.SpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zejian
 * Time  16/1/6 下午5:26
 * Email shinezejian@163.com
 * Description:表情主界面
 */
public class EmotionMainFragment extends BaseFragment implements View.OnClickListener {

    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT          ="bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN ="hide bar's editText and btn";

    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG ="CURRENT_POSITION_FLAG";
    //底部水平tab
//    private RecyclerView                  recyclerview_horizontal;
//    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //表情面板
    private EmotionKeyboard mEmotionKeyboard;

    private EditText     bar_edit_text;
    private Button       bar_btn_send;
    private ImageView    bar_image_add_btn;

    //需要绑定的内容view
    private View contentView;

    //不可横向滚动的ViewPager
    private NoHorizontalScrollerViewPager viewPager;

    //是否绑定当前Bar的编辑框,默认true,即绑定。
    //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isBindToBarEditText=true;

    //是否隐藏bar上的编辑框和发生按钮,默认不隐藏
    private boolean isHidenBarEditTextAndBtn=false;

    private List<Fragment> fragments =new ArrayList<>();

    private Bundle      args;
    private View        rootView;
    private MsgCallback mCallback;
    private int friendId;
    private String friendName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    /**
     * 创建与Fragment对象关联的View视图时调用
     * @param inflater view创造器
     * @param container view填充的地方
     * @param savedInstanceState fragment保存状态
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_emotion, container, false);
        isHidenBarEditTextAndBtn = args.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
        friendId = args.getInt("friendID");
        friendName = args.getString("friendName");
        //获取判断绑定对象的参数
        isBindToBarEditText=args.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);
        mCallback = (MsgCallback) args.getSerializable("callback");
        initView(rootView);
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(rootView.findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : ((EditText) rootView.findViewById(R.id.bar_edit_text)))//判断绑定那种EditView
                .bindToEmotionButton(rootView.findViewById(R.id.emotion_button))//绑定表情按钮
                .build();

        initListener();
        initDatas();
        //创建全局监听
        GlobalOnItemClickManagerUtils globalOnItemClickManager= GlobalOnItemClickManagerUtils.getInstance(getActivity());

        if(isBindToBarEditText){
            //绑定当前Bar的编辑框
            globalOnItemClickManager.attachToEditText(bar_edit_text);

        }else{
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
            globalOnItemClickManager.attachToEditText((EditText) contentView);
            mEmotionKeyboard.bindToEditText((EditText)contentView);
        }
        return rootView;
    }

    /**
     * 绑定内容view
     * @param contentView 当软键盘弹出来时，需要上移的布局，其父布局须是LinearLayout
     */
    public void bindToContentView(View contentView){
        this.contentView=contentView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView){
        viewPager= (NoHorizontalScrollerViewPager) rootView.findViewById(R.id.vp_emotionview_layout);
//        recyclerview_horizontal= (RecyclerView) rootView.findViewById(R.id.recyclerview_horizontal);
        bar_edit_text= (EditText) rootView.findViewById(R.id.bar_edit_text);
        bar_image_add_btn = (ImageView) rootView.findViewById(R.id.bar_image_add_btn);
        bar_btn_send= (Button) rootView.findViewById(R.id.bar_btn_send);
        bar_btn_send.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.themeColor));
        LinearLayout rl_editbar_bg = (LinearLayout) rootView.findViewById(R.id.rl_editbar_bg);
        if(isHidenBarEditTextAndBtn){//隐藏
            bar_edit_text.setVisibility(View.GONE);
            bar_image_add_btn.setVisibility(View.GONE);
            bar_btn_send.setVisibility(View.GONE);
            rl_editbar_bg.setBackgroundResource(R.color.white);
        }else{
            bar_edit_text.setVisibility(View.VISIBLE);
            bar_image_add_btn.setVisibility(View.VISIBLE);
            bar_btn_send.setVisibility(View.VISIBLE);
            rl_editbar_bg.setBackgroundResource(R.drawable.shape_bg_reply_edittext);
        }
    }

    /**
     * 初始化监听器
     */
    protected void initListener(){
        bar_btn_send.setOnClickListener(this);
        bar_image_add_btn.setOnClickListener(this);
        rootView.findViewById(R.id.emotion_button).setOnClickListener(this);
    }

    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    protected void initDatas(){
        replaceFragment();
//        List<ImageModel> list = new ArrayList<>();
//        for (int i=0 ; i<fragments.size(); i++){
//            if(i==0){
//                ImageModel model1=new ImageModel();
//                model1.icon= getResources().getDrawable(R.drawable.ic_emotion);
//                model1.flag="经典笑脸";
//                model1.isSelected=true;
//                list.add(model1);
//            }else {
//                ImageModel model = new ImageModel();
//                model.icon = getResources().getDrawable(R.drawable.ic_plus);
//                model.flag = "其他笑脸" + i;
//                model.isSelected = false;
//                list.add(model);
//            }
//        }

        //记录底部默认选中第一个
        int currentPosition = 0;
        SpUtil.setInteger(getActivity(), CURRENT_POSITION_FLAG, currentPosition);

        //底部tab
//        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(),list);
//        recyclerview_horizontal.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
//        recyclerview_horizontal.setAdapter(horizontalRecyclerviewAdapter);
//        recyclerview_horizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
//        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
//            @Override
//            public void onItemClick(View view, int position, List<ImageModel> datas) {
//                //获取先前被点击tab
//                int oldPosition = SharedPreferencedUtils.getInteger(getActivity(), CURRENT_POSITION_FLAG, 0);
//                //修改背景颜色的标记
//                datas.get(oldPosition).isSelected = false;
//                //记录当前被选中tab下标
//                CurrentPosition = position;
//                datas.get(CurrentPosition).isSelected = true;
//                SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
//                //通知更新，这里我们选择性更新就行了
//                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
//                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
//                //viewpager界面切换
//                viewPager.setCurrentItem(position,false);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
//            }
//        });

    }

    private void replaceFragment(){
        //创建fragment的工厂类
        FragmentFactory factory=FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        ChatToolsFragment toolsFragment = new ChatToolsFragment();
        fragments.add(toolsFragment);

        EmotiomComplateFragment f1= (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
//        Bundle b=null;
//        for (int i=0;i<7;i++){
//            b=new Bundle();
//            b.putString("Interge","Fragment-"+i);
//            Fragment1 fg= Fragment1.newInstance(Fragment1.class,b);
//            fragments.add(fg);
//        }

        NoHorizontalScrollerVPAdapter adapter =new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }


    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     * @return true则隐藏表情布局，拦截返回键操作
     *         false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress(){
        return mEmotionKeyboard.interceptBackPress();
    }

    @Override
    public void setResourceAndItemClick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bar_btn_send:
                SharedPreferences sp = SpUtil.getSharePreference(getActivity());
                int score = SpUtil.getSPScore(sp);
                if (score <= 0)
                    Toast.makeText(getActivity(),"您剩余的积分不足，请及时充值",Toast.LENGTH_SHORT).show();
                else{
                    String content = bar_edit_text.getText().toString();
                    bar_edit_text.setText("");
                    ChatEntity chatMessage = new ChatEntity();
                    chatMessage.setContent(content);
                    chatMessage.setSenderId(ApplicationData.getInstance().getUserInfo().getId());
                    chatMessage.setReceiverId(friendId);
                    chatMessage.setMessageType(ChatEntity.SEND);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss", Locale.CHINA);
                    String sendTime = sdf.format(date);
                    chatMessage.setSendTime(sendTime);
                    if (mCallback != null)
                        mCallback.onMsgCallback(chatMessage);

                    UserAction.sendMessage(chatMessage);
                    ImDB.getInstance(getActivity()).saveChatMessage(chatMessage);

                    MessageTabEntity messageTab = new MessageTabEntity();
                    messageTab.setContent(content);
                    messageTab.setMessageType(MessageTabEntity.FRIEND_MESSAGE);
                    messageTab.setName(friendName);
                    messageTab.setSenderId(friendId);
                    messageTab.setSendTime(sendTime);
                    messageTab.setUnReadCount(0);

                    //确认之前是否有和这个人聊过天，没有的话就添加一个
                    List<MessageTabEntity> messageEntities = ApplicationData.getInstance().getMessageEntities();
                    if (messageEntities.size() == 0) {
                        ImDB.getInstance(getActivity()).saveMessage(messageTab);
                        messageEntities.add(messageTab);
                        ApplicationData.getInstance().setMessageEntities(messageEntities);
                    } else {
                        for (MessageTabEntity entity : messageEntities) {
                            if (entity.getSenderId() != friendId) {
                                ImDB.getInstance(getActivity()).saveMessage(messageTab);
                                messageEntities.add(messageTab);
                                ApplicationData.getInstance().setMessageEntities(messageEntities);
                            }
                        }
                    }

                    //聊过的话就直接添加到messageEntities并且更新聊过天的数据库
                    ImDB.getInstance(getActivity()).updateMessages(messageTab);

                    if (ApplicationData.getInstance().getUserInfo().getGender() == 1) {
                        SpUtil.setIntSharedPreference(sp, "score", score - 1);
                        ApplicationData.getInstance().getUserInfo().setScore(score - 1);
                    }
                }
                break;
            case R.id.bar_image_add_btn:
                viewPager.setCurrentItem(0,false);//第0个是聊天工具的fragment
                mEmotionKeyboard.showChatToolsFragment();
                break;
            case R.id.emotion_button:
                viewPager.setCurrentItem(1,false);
                mEmotionKeyboard.showChatToolsFragment();
                break;
        }
    }

}


