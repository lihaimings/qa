package com.haiming.paper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.haiming.paper.R;
import com.haiming.paper.Utils.CommonUtil;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.thread.ThreadManager;

import java.util.Date;

public class InitData {

    private MyOpenHelper mHelper;

    private Context mContext;

    public InitData(Context context) {
        mHelper = new MyOpenHelper(context);
        this.mContext = context;
    }

    /**
     * 初始化分组
     */
    public void initGroup() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
                    "values(?,?,?,?,?,?)", new String[]{"首页", "1", "#FFFFFF", "0", CommonUtil.date2string(new Date()), CommonUtil.date2string(new Date())});

            db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
                    "values(?,?,?,?,?,?)", new String[]{"汽车", "1", "#FFFFFF", "0", CommonUtil.date2string(new Date()), CommonUtil.date2string(new Date())});
            Log.d("数据", "分组初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
    public void initUser() {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pic_cat);
            String path = UIUtil.bitmap2String(bitmap);

//            db.execSQL("create table db_user(u_id integer primary key autoincrement,u_name varchar,"+
//                    "u_password varchar,u_email varchar ,u_signature varchar, u_sex varchar,u_isManager integer,u_image varchar)");

            db.execSQL("insert into db_user(u_name, u_number,u_password, u_email, u_signature, u_sex, u_isManager,u_image) " +
                    "values(?,?,?,?,?,?,?,?)", new String[]{"水星","123456", "123456", "1056598@qq.com", "番茄炒鸡蛋", "男", "0", path});

            db.execSQL("insert into db_user(u_name,u_number, u_password, u_email, u_signature, u_sex, u_isManager,u_image) " +
                    "values(?,?,?,?,?,?,?,?)", new String[]{"天空","123", "123", "1056594@qq.com", "番茄炒鸡蛋", "女", "0", path});
            Log.d("数据", "用户初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void initManager() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            Bitmap mBitmap1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.m_one);
            String path1 = UIUtil.bitmap2String(mBitmap1);
            db.execSQL("insert into db_manager(m_name,m_number, m_password, m_isManager,u_image) " +
                    "values(?,?,?,?,?)", new String[]{"管理员1","8888", "8888", "1",path1});

            Bitmap mBitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.m_two);
            String path2 = UIUtil.bitmap2String(mBitmap2);
            db.execSQL("insert into db_manager(m_name, m_number,m_password, m_isManager,u_image) " +
                    "values(?,?,?,?,?)", new String[]{"管理员2","6666", "6666", "1",path2});
            Log.d("数据", "管理员初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void initAsk() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {

            String[] asks = {
                    "为什么中国没有自己的平价跑车？",
                    "你们有哪些事是买了车以后才知道的？",
                    "如何评价奇瑞瑞虎 7 ？",
                    "如何评价东风汽车集团宣布东风雷诺开始重组，雷诺品牌退出中国市场？",
                    "考驾驶证科目二紧张怎么办，有什么缓解的方法吗，谢谢大家？"
            };
            String[] desctipe={
                    "各大车场都有自己牌子的平民跑车，例如野马，本田86,马自达mx5等……价格其实相比很多奔驰，宝马来说并不算贵。是因为我们没有汽车文化？还是因为什么我们消费水平不够？亦或是汽车技术不过关？我们各车场并没有推出代表自己特点的平价跑车？是什么原因呢？",
                    "买车前，买车中，买车后你们有哪些后知后觉？来给大家分享一下！",
                    "",
                    "4月14日午间，伴随东风汽车集团股份有限公司（东风集团股份，http://0489.HK）的一纸公告，东风雷诺汽车有限公司（以下简称“东风雷诺”）开始重组。\n" +
                            "\n" +
                            "东风雷诺成立于2013年，东风集团股份有限公司与法国雷诺汽车公司（Renault S.A.以下简称雷诺）共同出资组建，双方各持股50%。如今，雷诺拟将持有的东风雷诺50%股权转让给东风集团股份，东风雷诺将停止雷诺品牌相关业务活动。\n" +
                            "\n",
                    ""
            };

            for (int i = 0; i < 5; i++) {
                String[] strings = {asks[i], desctipe[i]
                        , "2", "汽车", "2", "#FFFFFF", "0", CommonUtil.date2string(new Date()), CommonUtil.date2string(new Date()), "1", null, null};

                db.execSQL("insert into db_note(n_title, n_content, n_group_id, n_group_name, n_type, n_bg_color," +
                        "n_encrypt,n_create_time,n_update_time,n_user_id,n_answer_size,n_answer_id) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?)", strings);
            }

            String[] strings2 = {"刷 LeetCode 对于国内 IT 企业面试帮助大吗？", "今年大三，大四要找工作了，没搞过ACM（其实挺后悔的），校招面试都考算法的，我这种没搞过ACM的感觉挺没竞争力的，同学有推荐leetcode的，不知对于国内的IT企业面试帮助大吗？"
                    , "1", "首页", "2", "#FFFFFF", "0", CommonUtil.date2string(new Date()), CommonUtil.date2string(new Date()), "1", null, null};
            db.execSQL("insert into db_note(n_title, n_content, n_group_id, n_group_name, n_type, n_bg_color," +
                    "n_encrypt,n_create_time,n_update_time,n_user_id,n_answer_size,n_answer_id) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?)", strings2);
            strings2.clone();

            String[] strings3 = {"刷 LeetCode 对于国内 IT 企业面试帮助大吗？", "今年大三，大四要找工作了，没搞过ACM（其实挺后悔的），校招面试都考算法的，我这种没搞过ACM的感觉挺没竞争力的，同学有推荐leetcode的，不知对于国内的IT企业面试帮助大吗？"
                    , "1", "首页", "2", "#FFFFFF", "0", CommonUtil.date2string(new Date()), CommonUtil.date2string(new Date()), "1", null, null};
            for (int i = 0; i < 10; i++) {
                db.execSQL("insert into db_note(n_title, n_content, n_group_id, n_group_name, n_type, n_bg_color," +
                        "n_encrypt,n_create_time,n_update_time,n_user_id,n_answer_size,n_answer_id) " +
                        "values(?,?,?,?,?,?,?,?,?,?,?,?)", strings3);
            }

            Log.d("数据", "问题初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void initAnswer(Context context) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            NoteDao noteDao = new NoteDao(context);
            Log.d("列表", "开始增加答案");
            String[] strings = {"1",
                    "终于找回这个问题了。好问题。首先，知乎规矩，定义一下什么叫平价？\n【1】今天看到美国的平价超跑，克尔维特C7 Grand sport和C8 Z51。LS的大排量V8真是太香太香了。C8从上一代的前中置后驱改成中置后驱，双门双座，手动挡，绝对的超跑。LS发动机除了克尔维特自己用，还有科迈罗SS，还有凯迪拉克CTS-V这种车上用。还有一些皮卡也是用的V8同系列的发动机。这样才能均摊下去。对于美国人来说，这些美系性能车就是平价跑车，平价肌肉车，平价小马车。欧洲的跑车，超跑，小钢炮相比之下都贵的要死或溢价较高。\n【2】冷静点来说，中国目前还没有这个土壤。就算美国，玩改装的也只是少数，而且还是主要偏性能方面的改装，而不是外观和姿态。非常非常小众，就像斯巴鲁改装车迷那种小众的。领克03+算是国产的一次比较成功，还要继续看的尝试。关键还在价格，消费力啊，伙计。就是懂得或者投缘的觉得这种车非常非常值，二十万买个奥迪S3级别的四驱钢炮，比GTI有性价比，马力挺大，配置挺高。算上开发成本均摊，车厂其实卖一辆亏一辆。但是不了解的觉得你疯了吧，花二十万买个国产的紧凑级轿车，领克人家还以为是吉利的呢，不就马力大一点吗。人家二十万能买雅阁凯美瑞，奥迪宝马的入门车型。" +
                            "\n【3】平价你要看有多平价。我非常认同一句话，要浪漫先浪费。跑车还在乎啥实用性？你看看正宗的双门双座敞篷小跑车，马自达MX-5卖多少钱？三十三，三十五万啊，不和别的轿车或小钢炮去比，就和同样从日本进口的Brz和86比，同样的2.0排量，贵了六七万，brz不加价只要二十七万左右……要是真在乎实用性，86brz的后排实用性也不怎么样，还是看看GTI，后排能正经坐人。中国以前的平价轿跑大着胆子才卖多少钱呢？定价多少？十万？十五万？还想要跑车？K-car跑车吧。过去咱们的发动机也不行，匹配的变速箱调教也不行，真的没这条件。或者说让车厂可着劲去玩，让红旗那样的企业去造跑车吗？\n【4】车厂造车就是生意，要赚钱的。没人买，除非去跑比赛的那种。不过人家超跑也是现成立车队，然后造车，就是典型的以贩养吸。造车卖车是为了跑比赛，而不是跑比赛是为了卖车。这点要搞清楚。到了国内还有困难。一是没有欧洲或美国的赛车文化，过去也没有那么多大奖赛。二是小厂可能没有造车资格，除非像新势力一样并购买人家的造车资格。\n【5】汽车文化，既然是文化就要有土壤。汽车是什么，是你可能每天都要开的东西。就算你去国外玩，整天飞来飞去，回家了出门也要有个工具代步啊，汽车就是这样的东西。因此，有什么样的社会，什么经济基础就有什么样的车。每个国家和地方的道路情况，交规，公共交通，甚至治安和安全程度都不一样，这些都反应在汽车上面的。美国怎么样？美国也崇洋媚外啊，日本也一样，大家都喜欢欧洲来的豪华品牌出的跑车，不是吗？没有美国买家，保时捷早死了。国人对中国品牌造这些华而不实的支持吗？没有特别支持吧，没有韩国那种国内市场保护吧。韩国富人也非常不爱国，至少最近这些年非常崇洋媚外。\n【6】日本当年为什么能造跑车？是因为广场协议，日元升值被逼出来的，车不往高端走，定位和品牌形象不变高，日本以外不买单。丰田都造中置后驱的MR-2跑车，一个家用车的品牌，你敢信？日产GTR skyline一千匹各种秒。EVO和STI相爱相杀。一方面是技术储备和发展到那里了，另一方面是学有余力，全社会有钱有闲玩车，家庭都到了买第三第四第五部车的时候了。现在的JDM只不过是当年日本疯狂的玩车文化的一小部分残余，日本年轻人好像基本都不买车了，也不感兴趣。【7】现状。我国一方面大城市限牌限购，小城市很多地方还处于人生第一辆甚至家庭第一辆车。考过驾照，放着好几年没开过。或者第一辆还在选二手车过度一下的阶段。如果是家庭唯一一辆车，给你钱你也不会买个只有两个座，后备箱特别小的小跑车吧？MX-5本来就是属于英式跑车，在美国加州开发的，首先面向美国市场的。美国人均汽车保有量是什么水平？对吧。【8】既然车厂是要赚钱的，中国人造中国自己的跑车不挣钱。那么就需要其他车型养着。你说的造跑车的那几个厂家和国家是什么情况呢？保时捷用Macan卡宴和panamera以及718去养着911越造越好。野马是靠福特F150的利润。克尔维特，科迈罗那些是靠silverado皮卡。丰田就不用说了吧，而且86产量并不大。斯巴鲁和马自达过得并不好，当然三菱更惨。造跑车有什么好下场吗？连特斯拉都不能靠改装跑车盈利。造车新势力也学聪明了，suv才是大趋势，蔚来EP－9造出来也不卖。前途K90好像也没有消息了，还有华人运通什么的？不太关注新能源，没意思。车企造新能源车也不盈利，也是持平甚至亏钱的。别看比亚迪喊的响，汽油车没少造，因为汽油单动利润高啊。电池成本太高了，整车的50%左右，没法玩了。劝车企冒进造跑车真是……将来是要负责任的。【9】要浪漫先浪费，没错的。结论，穷。社会环境，房价，工作和生活压力，养车环境不友好。所以没有平价跑车。相反，我国有平价手机，平价平板，平价电子产品。思考题，你可以想想为什么？"
                    , "1"};

            for (int i = 0; i < 10; i++) {
                db.execSQL("insert into db_answer(a_note_id, a_content, a_user_id) " +
                        "values(?,?,?)", strings);
                int finalI = i;
                if (finalI == 0){
                    noteDao.updataAnswerSize(1,strings[1]);
                }else {
                    noteDao.updataAnswerSize(1,"");
                    Log.d("列表","id=1的增加问题的回答数完成第"+finalI);
                }
                ThreadManager.getLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                    }
                });


            }
            strings.clone();

            String[] strings2 = {"2", "你不用担心，求职说到底是一种买卖交易，只要你能体现你的价值，商品就能卖出去。"
                    , "2"};
            for (int i = 0; i < 10; i++) {
                db.execSQL("insert into db_answer(a_note_id, a_content, a_user_id) " +
                        "values(?,?,?)", strings2);
                int finalI = i;
                if (i == 0){
                    noteDao.updataAnswerSize(2,strings2[1]);
                }else {
                    noteDao.updataAnswerSize(2);
                    Log.d("列表","id=2的增加问题的回答数完成第"+finalI);
                }
            }
            strings2.clone();
            Log.d("列表", "增加答案完成");
        } catch (Exception e) {
            Log.d("列表", "增加答案异常");
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
