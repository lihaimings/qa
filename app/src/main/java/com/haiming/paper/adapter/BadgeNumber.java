package com.haiming.paper.adapter;

public class BadgeNumber {

    public final static int DISPLAY_MODE_DOT = 0;
    public final static int DISPLAY_MODE_NUMBER = 1;

    public final static int TYPE_INVISIBLE = 0;
    public final static int TYPE_VISIBLE = 1;


    private BadgeNumberData badgeNumberData;
    private BadgeNumberChildInterval badgeNumberChildInterval;

    public BadgeNumberData getBadgeNumberData() {
        return badgeNumberData;
    }

    public void setBadgeNumberData(BadgeNumberData badgeNumberData) {
        this.badgeNumberData = badgeNumberData;
    }


    public BadgeNumberChildInterval getBadgeNumberChildInterval() {
        return badgeNumberChildInterval;
    }

    public void setBadgeNumberChildInterval(BadgeNumberChildInterval badgeNumberChildInterval) {
        this.badgeNumberChildInterval = badgeNumberChildInterval;
    }


    /**
     * badge number类型区间。
     */
    public static class BadgeNumberChildInterval {
        private int indexMin;
        private int indexMax;

        public BadgeNumberChildInterval(int indexMin,int indexMax){
            this.indexMin = indexMin;
            this.indexMax = indexMax;
        }

        public int getIndexMin() {
            return indexMin;
        }

        public void setIndexMin(int indexMin) {
            this.indexMin = indexMin;
        }

        public int getIndexMax() {
            return indexMax;
        }

        public void setIndexMax(int indexMax) {
            this.indexMax = indexMax;
        }
    }

    public static class BadgeNumberData{
        private int type;
        private int count;
        private int displayMode;


        /**
         *数字型比较常用，特别写了个数字显示方便的构造函数
         */
        public BadgeNumberData(int count){
            this(count, DISPLAY_MODE_NUMBER);
        }

        public BadgeNumberData(int count,int displayMode){
            this.type = count == 0 ? TYPE_INVISIBLE:TYPE_VISIBLE;
            this.count = count;
            this.displayMode = displayMode;
        }

        public BadgeNumberData(int type,int count,int displayMode){
            this.type = type;
            this.count = count;
            this.displayMode = displayMode;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getDisplayMode() {
            return displayMode;
        }

        public void setDisplayMode(int type) {
            this.displayMode = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
