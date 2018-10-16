//package com.jiangxufa.demomodule;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.jiangxufa.baselibrary.common.BaseRefreshActivity;
//
//import org.jetbrains.annotations.NotNull;
//
//public class MainActivity extends BaseRefreshActivity<String,MainActivity.Ad> {
//
//    private Ad ad;
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////
////        RecyclerView rcv = findViewById(R.id.rcv);
////        rcv.setItemAnimator(new DefaultItemAnimator());
////        rcv.setLayoutManager(new LinearLayoutManager(this));
////        ad = new Ad();
////        List<String> list = new ArrayList<>();
////        for (int i = 0; i < 20; i++) {
////            list.add("这是一个"+i);
////        }
////        ad.setNewData(list);
////        rcv.setAdapter(ad);
////
////
////    }
////
////    public void remove(View view) {
////        ad.removeII();
////    }
////
////
////    public void add(View view) {
////        ad.addII();
////    }
//
//    @NotNull
//    @Override
//    public Ad getAdapter() {
//        return new Ad();
//    }
//
//    class Ad extends BaseQuickAdapter<String,BaseViewHolder>{
//
//
//        public Ad() {
//            super(R.layout.item_text);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, String item) {
//            helper.setText(R.id.tv, item);
//        }
//
//        public void addII() {
//            mData.add("加一个");
//            notifyItemInserted(0);
//        }
//
//        public void removeII() {
//            mData.remove(3);
//            notifyItemRemoved(3);
//        }
//    }
//}
