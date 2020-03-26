//package com.haiming.paper.ui.view;
//
//import com.google.android.material.badge.BadgeUtils;
//
//public class BadgeNumberTreeManager {
//
//    private static final BadgeNumberTreeManager instance = new BadgeNumberTreeManager();
//    private BadgeNumberTreeManager(){}
//
//    public static BadgeNumberTreeManager getInstance(){
//        return instance;
//    }
//
//    private List<List<BadgeNumber>> badgeTree;
//    private int treeDepth;
//
//    public void setBadgeTree(List<List<BadgeNumber>> badgeTree){
//        this.badgeTree = badgeTree;
//        treeDepth = badgeTree.size();
//        //UIUtil.getContext().sendBroadcast(new Intent(Constant.ACTION_REFRESH_BADGE));
//        EventBus.getDefault().post(new BadgeEvent());
//    }
//
//    public BadgeNumber.BadgeNumberData getBadgeNum(int depth, int index){
//        if(badgeTree!=null) {
//            if (badgeTree.get(depth).get(index).getBadgeNumberChildInterval().getIndexMin() == -1
//                    && badgeTree.get(depth).get(index).getBadgeNumberChildInterval().getIndexMax() == -1) {
//                //叶子节点
//                return new BadgeNumber.BadgeNumberData(badgeTree.get(depth).get(index).getBadgeNumberData().getType(),
//                        badgeTree.get(depth).get(index).getBadgeNumberData().getCount(),
//                        badgeTree.get(depth).get(index).getBadgeNumberData().getDisplayMode());
//            } else {
//                //非叶子节点
//                int tempType = 0;
//                int tempCount = 0;
//                int tempDisplayMode = 0;
//                for (int i = badgeTree.get(depth).get(index).getBadgeNumberChildInterval().getIndexMin();
//                     i <= badgeTree.get(depth).get(index).getBadgeNumberChildInterval().getIndexMax(); i++) {
//                    BadgeNumber.BadgeNumberData childData = getBadgeNum(depth + 1, i);
//                    tempType += childData.getType();
//                    tempCount += childData.getCount();
//                    if (childData.getType() != BadgeNumber.TYPE_INVISIBLE)
//                        tempDisplayMode += childData.getDisplayMode();
//                }
//                if (tempType >= 1) tempType = 1;
//                if (tempDisplayMode >= 1) tempDisplayMode = 1;
//                return new BadgeNumber.BadgeNumberData(tempType, tempCount, tempDisplayMode);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 更新在pos位置上的节点的数据
//     *
//     * @param pos 更新的节点在树中的位置
//     * @param data 更新的数据
//     */
//    public void updateBadgeNum(PosInTree pos, BadgeNumber.BadgeNumberData data){
//        if(badgeTree!=null){
//            badgeTree.get(pos.getDepth()).get(pos.getIndex()).setBadgeNumberData(data);
//            //非根节点时要更新父节点数据
//            /*if(!(badgeTree.get(pos.getDepth()).get(pos.getIndex()).getBadgeNumberChildInterval().getIndexMin()==-1
//                    &&badgeTree.get(pos.getDepth()).get(pos.getIndex()).getBadgeNumberChildInterval().getIndexMax()==-1)){
//                //遍历上一层
//                for(int i = 0;i<badgeTree.get(pos.getDepth()-1).size();i++){
//                    //循环判断是否在上层节点的子节点
//                    if(pos.getIndex()>=badgeTree.get(pos.getDepth()-1).get(i).getBadgeNumberChildInterval().getIndexMin()
//                            && pos.getIndex()<=badgeTree.get(pos.getDepth()-1).get(i).getBadgeNumberChildInterval().getIndexMax()){
//                        //是，更新该父节点
//                        PosInTree fatherPos = new PosInTree(pos.getDepth()-1,i);
//                        updateBadgeNum(fatherPos,getBadgeNum(fatherPos.getDepth(),fatherPos.getIndex()));
//                        break;
//                    }
//                }
//            }*/
//            //UIUtil.getContext().sendBroadcast(new Intent(Constant.ACTION_REFRESH_BADGE));
//            EventBus.getDefault().post(new BadgeEvent());
//
//        }
//    }
//
//    /**
//     * 增加某节点上的计数
//     *
//     * @param pos
//     * @param addNum
//     */
//    public void addBadgeNum(PosInTree pos,int addNum){
//        BadgeNumber.BadgeNumberData data = BadgeNumberTreeManager.getInstance().getBadgeNum(pos.getDepth(),pos.getIndex());
//        if (data!=null){
//            BadgeUtils.setBadgeNumberCount(data,data.getCount()+addNum);
//            updateBadgeNum(pos,data);
//        }
//    }
//
//    /**
//     * 减少某节点上的计数
//     *
//     * @param pos
//     * @param minusNum
//     */
//    public void minusBadgeNum(PosInTree pos,int minusNum){
//        BadgeNumber.BadgeNumberData data = BadgeNumberTreeManager.getInstance().getBadgeNum(pos.getDepth(),pos.getIndex());
//        if (data!=null){
//            BadgeUtils.setBadgeNumberCount(data,data.getCount()-minusNum);
//            updateBadgeNum(pos,data);
//        }
//    }
//
//    /**
//     * 清楚某节点的计数
//     * @param pos
//     */
//    public void clearBadgeNum(PosInTree pos){
//        BadgeNumber.BadgeNumberData data = BadgeNumberTreeManager.getInstance().getBadgeNum(pos.getDepth(),pos.getIndex());
//        if (data!=null){
//            BadgeUtils.setBadgeNumberCount(data,0);
//            updateBadgeNum(pos,data);
//        }
//    }
//
//    /**
//     * 清楚所有计数
//     */
//    public void clearAll(){
//        //System.out.println("badge clear all");
//        initBadgeTree();
//        //UIUtil.getContext().sendBroadcast(new Intent(Constant.ACTION_REFRESH_BADGE));
//        EventBus.getDefault().post(new BadgeEvent());
//    }
//    private void initBadgeTree(){
//        BadgeTargetBean badgeTargetBean = new BadgeTargetBean();
//        badgeTargetBean.setTotal("0");
//        badgeTargetBean.setCatch_code_badge_num("0");
//
//        badgeTargetBean.setCommodity_order_badge_num("0");
//        badgeTargetBean.setCommodity_proceed_order_badge_num("0");
//        badgeTargetBean.setCommodity_finished_order_badge_num("0");
//        badgeTargetBean.setCommodity_other_order_badge_num("0");
//
//        badgeTargetBean.setGame_download_badge_num("0");
//        badgeTargetBean.setLevel_upgrade_badge_num("0");
//        badgeTargetBean.setMedals_badge_num("0");
//        badgeTargetBean.setMoment_action_badge_num("0");
//        badgeTargetBean.setZan_action_badge_num("0");
//        badgeTargetBean.setComment_action_badge_num("0");
//        badgeTargetBean.setPrize_badge_num("0");
//        badgeTargetBean.setTao_code_badge_num("0");
//        badgeTargetBean.setWin_awards_badge_num("0");
//        badgeTargetBean.setApp_message("0");
//        badgeTargetBean.setViewed_contact_badge_num("0");
//        badgeTargetBean.setCollect_news_badge_num("0");
//
//        badgeTargetBean.setExtra_reward_badge_num("0");
//
//        /*badgeTargetBean.setMy_sale_badge_num("0");
//        badgeTargetBean.setSale_on_badge_num("0");
//        badgeTargetBean.setSale_waiting_badge_num("0");
//        badgeTargetBean.setSale_out_badge_num("0");*/
//
//        BadgeNumberTreeManager.getInstance().setBadgeTree(BadgeUtils.buildTree(badgeTargetBean));
//
//        BadgeNumberTreeManager.getInstance().clearBadgeNum(BadgeUtils.posMap.get(BadgeUtils.BADGE_MY_IM_UNREAD_MESSAGE));
//
//    }
//
//
//    public static class PosInTree{
//        private int depth;
//        private int index;
//
//        public PosInTree(int depth,int index){
//            this.depth = depth;
//            this.index = index;
//        }
//
//        public int getDepth() {
//            return depth;
//        }
//
//        public void setDepth(int depth) {
//            this.depth = depth;
//        }
//
//        public int getIndex() {
//            return index;
//        }
//
//        public void setIndex(int index) {
//            this.index = index;
//        }
//    }
//
//}
//
