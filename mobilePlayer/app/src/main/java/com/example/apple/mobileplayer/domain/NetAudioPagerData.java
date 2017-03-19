package com.example.apple.mobileplayer.domain;


import java.util.List;

/**
 * Created by apple on 17/2/20.
 * 网络音乐数据
 */

public class NetAudioPagerData {

    public NetAudioPagerData() {
    }

    private InfoBean info;
    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {
        /**
         * count : 4493
         * np : 1487569742
         */

        private int count;
        private int np;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNp() {
            return np;
        }

        public void setNp(int np) {
            this.np = np;
        }
    }

    public static class ListBean {
        /**
         * status : 4
         * comment : 12
         * tags : [{"id":1,"name":"搞笑"},{"id":60,"name":"吐槽"},{"id":61,"name":"恶搞"},{"id":62,"name":"内涵"},{"id":473,"name":"社会新闻"}]
         * bookmark : 2
         * text : 小伙用全部工资打赏女主播被退回:有些钱不能赚
         * image : {"medium":[],"big":["http://wimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_1.jpg","http://dimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_1.jpg"],"download_url":["http://wimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_d.jpg","http://dimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_d.jpg","http://wimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9.jpg","http://dimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9.jpg"],"height":7417,"width":864,"small":[],"thumbnail_small":["http://wimg.spriteapp.cn/crop/150x150/ugc/2017/02/19/58a8eb1569fb9.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2017/02/19/58a8eb1569fb9.jpg"]}
         * up : 68
         * share_url : http://a.f.budejie.com/share/23740925.html?wx.qq.com
         * down : 23
         * forward : 3
         * u : {"header":["http://wimg.spriteapp.cn/profile/large/2016/05/23/574214872055d_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/05/23/574214872055d_mini.jpg"],"uid":"17246660","is_vip":false,"is_v":true,"room_url":"","room_name":"啦啦啦","room_role":"帮主","room_icon":"http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_9.png","name":"雁回西楼 [啦啦啦]"}
         * passtime : 2017-02-20 17:42:01
         * type : image
         * id : 23740925
         * video : {"playfcount":3991,"height":360,"width":640,"video":["http://wvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4","http://dvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4"],"download":["http://wvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4","http://dvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4"],"duration":19,"playcount":15525,"thumbnail":["http://wimg.spriteapp.cn/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg","http://dimg.spriteapp.cn/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg"],"thumbnail_small":["http://wimg.spriteapp.cn/crop/150x150/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg","http://dimg.spriteapp.cn/crop/150x150/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg"]}
         * top_comments : [{"voicetime":0,"status":0,"hate_count":0,"cmt_type":"text","precid":0,"content":"看着真恶心","like_count":23,"u":{"header":["http://wx.qlogo.cn/mmopen/ZeuSZYeUCFKfGe1ufic7YAeeWz1pdwhKAAb04SricY5EFwqGslqgxfkVciaIKtI5neV5fUNsagPIPsesLh2vEyGQAHVicZQyCWsic/0","http://wx.qlogo.cn/mmopen/ZeuSZYeUCFKfGe1ufic7YAeeWz1pdwhKAAb04SricY5EFwqGslqgxfkVciaIKtI5neV5fUNsagPIPsesLh2vEyGQAHVicZQyCWsic/0"],"uid":"19870008","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"六道z9y"},"preuid":0,"passtime":"2017-02-20 13:41:04","voiceuri":"","id":74884877}]
         * gif : {"images":["http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc.gif","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc.gif"],"width":252,"gif_thumbnail":["http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg"],"download_url":["http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_d.jpg","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_d.jpg","http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg"],"height":272}
         * vote : [{"name":"赞同。","vid":4414,"vote_num":2770},{"name":"不赞同。","vid":4415,"vote_num":1936}]
         * top_comment : {"voicetime":0,"status":0,"hate_count":7,"cmt_type":"text","precid":0,"content":"就该整治这种整天在网上胡说八道的人！让他赔！","like_count":297,"u":{"header":["http://wimg.spriteapp.cn/profile/large/2015/09/06/55ec3364bf74f_mini.jpg","http://dimg.spriteapp.cn/profile/large/2015/09/06/55ec3364bf74f_mini.jpg"],"uid":"5669861","is_vip":false,"room_url":"","sex":"f","room_name":"","room_role":"","room_icon":"","name":"不爱穿内裤的人"},"preuid":0,"passtime":"2017-02-19 22:17:14","voiceuri":"","id":74849366}
         */

        private int status;
        private String comment;
        private String bookmark;
        private String text;
        private ImageBean image;
        private String up;
        private String share_url;
        private int down;
        private int forward;
        private UBean u;
        private String passtime;
        private String type;
        private String id;
        private VideoBean video;
        private GifBean gif;
        private TopCommentBean top_comment;
        private List<TagsBean> tags;
        private List<TopCommentsBean> top_comments;
        private List<VoteBean> vote;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getBookmark() {
            return bookmark;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getDown() {
            return down;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public int getForward() {
            return forward;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }

        public UBean getU() {
            return u;
        }

        public void setU(UBean u) {
            this.u = u;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public GifBean getGif() {
            return gif;
        }

        public void setGif(GifBean gif) {
            this.gif = gif;
        }

        public TopCommentBean getTop_comment() {
            return top_comment;
        }

        public void setTop_comment(TopCommentBean top_comment) {
            this.top_comment = top_comment;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<TopCommentsBean> getTop_comments() {
            return top_comments;
        }

        public void setTop_comments(List<TopCommentsBean> top_comments) {
            this.top_comments = top_comments;
        }

        public List<VoteBean> getVote() {
            return vote;
        }

        public void setVote(List<VoteBean> vote) {
            this.vote = vote;
        }

        public static class ImageBean {
            /**
             * medium : []
             * big : ["http://wimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_1.jpg","http://dimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_d.jpg","http://dimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9_d.jpg","http://wimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9.jpg","http://dimg.spriteapp.cn/ugc/2017/02/19/58a8eb1569fb9.jpg"]
             * height : 7417
             * width : 864
             * small : []
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/ugc/2017/02/19/58a8eb1569fb9.jpg","http://dimg.spriteapp.cn/crop/150x150/ugc/2017/02/19/58a8eb1569fb9.jpg"]
             */

            private int height;
            private int width;
            private List<?> medium;
            private List<String> big;
            private List<String> download_url;
            private List<?> small;
            private List<String> thumbnail_small;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<?> getMedium() {
                return medium;
            }

            public void setMedium(List<?> medium) {
                this.medium = medium;
            }

            public List<String> getBig() {
                return big;
            }

            public void setBig(List<String> big) {
                this.big = big;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public List<?> getSmall() {
                return small;
            }

            public void setSmall(List<?> small) {
                this.small = small;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }

            @Override
            public String toString() {
                return "ImageBean{" +
                        "height=" + height +
                        ", width=" + width +
                        ", medium=" + medium +
                        ", big=" + big +
                        ", download_url=" + download_url +
                        ", small=" + small +
                        ", thumbnail_small=" + thumbnail_small +
                        '}';
            }
        }

        public static class UBean {
            /**
             * header : ["http://wimg.spriteapp.cn/profile/large/2016/05/23/574214872055d_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/05/23/574214872055d_mini.jpg"]
             * uid : 17246660
             * is_vip : false
             * is_v : true
             * room_url :
             * room_name : 啦啦啦
             * room_role : 帮主
             * room_icon : http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_9.png
             * name : 雁回西楼 [啦啦啦]
             */

            private String uid;
            private boolean is_vip;
            private boolean is_v;
            private String room_url;
            private String room_name;
            private String room_role;
            private String room_icon;
            private String name;
            private List<String> header;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public boolean isIs_v() {
                return is_v;
            }

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public String getRoom_url() {
                return room_url;
            }

            public void setRoom_url(String room_url) {
                this.room_url = room_url;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_role() {
                return room_role;
            }

            public void setRoom_role(String room_role) {
                this.room_role = room_role;
            }

            public String getRoom_icon() {
                return room_icon;
            }

            public void setRoom_icon(String room_icon) {
                this.room_icon = room_icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getHeader() {
                return header;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }
        }

        public static class VideoBean {
            /**
             * playfcount : 3991
             * height : 360
             * width : 640
             * video : ["http://wvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4","http://dvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4"]
             * download : ["http://wvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4","http://dvideo.spriteapp.cn/video/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.mp4"]
             * duration : 19
             * playcount : 15525
             * thumbnail : ["http://wimg.spriteapp.cn/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg","http://dimg.spriteapp.cn/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg"]
             * thumbnail_small : ["http://wimg.spriteapp.cn/crop/150x150/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg","http://dimg.spriteapp.cn/crop/150x150/picture/2017/0218/8707c668-f5dc-11e6-86e6-90b11c479401_wpd.jpg"]
             */

            private int playfcount;
            private int height;
            private int width;
            private int duration;
            private int playcount;
            private List<String> video;
            private List<String> download;
            private List<String> thumbnail;
            private List<String> thumbnail_small;

            public int getPlayfcount() {
                return playfcount;
            }

            public void setPlayfcount(int playfcount) {
                this.playfcount = playfcount;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getPlaycount() {
                return playcount;
            }

            public void setPlaycount(int playcount) {
                this.playcount = playcount;
            }

            public List<String> getVideo() {
                return video;
            }

            public void setVideo(List<String> video) {
                this.video = video;
            }

            public List<String> getDownload() {
                return download;
            }

            public void setDownload(List<String> download) {
                this.download = download;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public List<String> getThumbnail_small() {
                return thumbnail_small;
            }

            public void setThumbnail_small(List<String> thumbnail_small) {
                this.thumbnail_small = thumbnail_small;
            }
        }

        public static class GifBean {
            /**
             * images : ["http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc.gif","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc.gif"]
             * width : 252
             * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_d.jpg","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_d.jpg","http://wimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg","http://dimg.spriteapp.cn/ugc/2017/02/20/58aa7fd356ccc_a_1.jpg"]
             * height : 272
             */

            private int width;
            private int height;
            private List<String> images;
            private List<String> gif_thumbnail;
            private List<String> download_url;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public List<String> getGif_thumbnail() {
                return gif_thumbnail;
            }

            public void setGif_thumbnail(List<String> gif_thumbnail) {
                this.gif_thumbnail = gif_thumbnail;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }
        }

        public static class TopCommentBean {
            /**
             * voicetime : 0
             * status : 0
             * hate_count : 7
             * cmt_type : text
             * precid : 0
             * content : 就该整治这种整天在网上胡说八道的人！让他赔！
             * like_count : 297
             * u : {"header":["http://wimg.spriteapp.cn/profile/large/2015/09/06/55ec3364bf74f_mini.jpg","http://dimg.spriteapp.cn/profile/large/2015/09/06/55ec3364bf74f_mini.jpg"],"uid":"5669861","is_vip":false,"room_url":"","sex":"f","room_name":"","room_role":"","room_icon":"","name":"不爱穿内裤的人"}
             * preuid : 0
             * passtime : 2017-02-19 22:17:14
             * voiceuri :
             * id : 74849366
             */

            private int voicetime;
            private int status;
            private int hate_count;
            private String cmt_type;
            private int precid;
            private String content;
            private int like_count;
            private UBeanX u;
            private int preuid;
            private String passtime;
            private String voiceuri;
            private int id;

            public int getVoicetime() {
                return voicetime;
            }

            public void setVoicetime(int voicetime) {
                this.voicetime = voicetime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getHate_count() {
                return hate_count;
            }

            public void setHate_count(int hate_count) {
                this.hate_count = hate_count;
            }

            public String getCmt_type() {
                return cmt_type;
            }

            public void setCmt_type(String cmt_type) {
                this.cmt_type = cmt_type;
            }

            public int getPrecid() {
                return precid;
            }

            public void setPrecid(int precid) {
                this.precid = precid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public UBeanX getU() {
                return u;
            }

            public void setU(UBeanX u) {
                this.u = u;
            }

            public int getPreuid() {
                return preuid;
            }

            public void setPreuid(int preuid) {
                this.preuid = preuid;
            }

            public String getPasstime() {
                return passtime;
            }

            public void setPasstime(String passtime) {
                this.passtime = passtime;
            }

            public String getVoiceuri() {
                return voiceuri;
            }

            public void setVoiceuri(String voiceuri) {
                this.voiceuri = voiceuri;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public static class UBeanX {
                /**
                 * header : ["http://wimg.spriteapp.cn/profile/large/2015/09/06/55ec3364bf74f_mini.jpg","http://dimg.spriteapp.cn/profile/large/2015/09/06/55ec3364bf74f_mini.jpg"]
                 * uid : 5669861
                 * is_vip : false
                 * room_url :
                 * sex : f
                 * room_name :
                 * room_role :
                 * room_icon :
                 * name : 不爱穿内裤的人
                 */

                private String uid;
                private boolean is_vip;
                private String room_url;
                private String sex;
                private String room_name;
                private String room_role;
                private String room_icon;
                private String name;
                private List<String> header;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public boolean isIs_vip() {
                    return is_vip;
                }

                public void setIs_vip(boolean is_vip) {
                    this.is_vip = is_vip;
                }

                public String getRoom_url() {
                    return room_url;
                }

                public void setRoom_url(String room_url) {
                    this.room_url = room_url;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getRoom_name() {
                    return room_name;
                }

                public void setRoom_name(String room_name) {
                    this.room_name = room_name;
                }

                public String getRoom_role() {
                    return room_role;
                }

                public void setRoom_role(String room_role) {
                    this.room_role = room_role;
                }

                public String getRoom_icon() {
                    return room_icon;
                }

                public void setRoom_icon(String room_icon) {
                    this.room_icon = room_icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<String> getHeader() {
                    return header;
                }

                public void setHeader(List<String> header) {
                    this.header = header;
                }
            }
        }

        public static class TagsBean {
            /**
             * id : 1
             * name : 搞笑
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class TopCommentsBean {
            /**
             * voicetime : 0
             * status : 0
             * hate_count : 0
             * cmt_type : text
             * precid : 0
             * content : 看着真恶心
             * like_count : 23
             * u : {"header":["http://wx.qlogo.cn/mmopen/ZeuSZYeUCFKfGe1ufic7YAeeWz1pdwhKAAb04SricY5EFwqGslqgxfkVciaIKtI5neV5fUNsagPIPsesLh2vEyGQAHVicZQyCWsic/0","http://wx.qlogo.cn/mmopen/ZeuSZYeUCFKfGe1ufic7YAeeWz1pdwhKAAb04SricY5EFwqGslqgxfkVciaIKtI5neV5fUNsagPIPsesLh2vEyGQAHVicZQyCWsic/0"],"uid":"19870008","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"六道z9y"}
             * preuid : 0
             * passtime : 2017-02-20 13:41:04
             * voiceuri :
             * id : 74884877
             */

            private int voicetime;
            private int status;
            private int hate_count;
            private String cmt_type;
            private int precid;
            private String content;
            private int like_count;
            private UBeanXX u;
            private int preuid;
            private String passtime;
            private String voiceuri;
            private int id;

            public int getVoicetime() {
                return voicetime;
            }

            public void setVoicetime(int voicetime) {
                this.voicetime = voicetime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getHate_count() {
                return hate_count;
            }

            public void setHate_count(int hate_count) {
                this.hate_count = hate_count;
            }

            public String getCmt_type() {
                return cmt_type;
            }

            public void setCmt_type(String cmt_type) {
                this.cmt_type = cmt_type;
            }

            public int getPrecid() {
                return precid;
            }

            public void setPrecid(int precid) {
                this.precid = precid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public UBeanXX getU() {
                return u;
            }

            public void setU(UBeanXX u) {
                this.u = u;
            }

            public int getPreuid() {
                return preuid;
            }

            public void setPreuid(int preuid) {
                this.preuid = preuid;
            }

            public String getPasstime() {
                return passtime;
            }

            public void setPasstime(String passtime) {
                this.passtime = passtime;
            }

            public String getVoiceuri() {
                return voiceuri;
            }

            public void setVoiceuri(String voiceuri) {
                this.voiceuri = voiceuri;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public static class UBeanXX {
                /**
                 * header : ["http://wx.qlogo.cn/mmopen/ZeuSZYeUCFKfGe1ufic7YAeeWz1pdwhKAAb04SricY5EFwqGslqgxfkVciaIKtI5neV5fUNsagPIPsesLh2vEyGQAHVicZQyCWsic/0","http://wx.qlogo.cn/mmopen/ZeuSZYeUCFKfGe1ufic7YAeeWz1pdwhKAAb04SricY5EFwqGslqgxfkVciaIKtI5neV5fUNsagPIPsesLh2vEyGQAHVicZQyCWsic/0"]
                 * uid : 19870008
                 * is_vip : false
                 * room_url :
                 * sex : m
                 * room_name :
                 * room_role :
                 * room_icon :
                 * name : 六道z9y
                 */

                private String uid;
                private boolean is_vip;
                private String room_url;
                private String sex;
                private String room_name;
                private String room_role;
                private String room_icon;
                private String name;
                private List<String> header;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public boolean isIs_vip() {
                    return is_vip;
                }

                public void setIs_vip(boolean is_vip) {
                    this.is_vip = is_vip;
                }

                public String getRoom_url() {
                    return room_url;
                }

                public void setRoom_url(String room_url) {
                    this.room_url = room_url;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getRoom_name() {
                    return room_name;
                }

                public void setRoom_name(String room_name) {
                    this.room_name = room_name;
                }

                public String getRoom_role() {
                    return room_role;
                }

                public void setRoom_role(String room_role) {
                    this.room_role = room_role;
                }

                public String getRoom_icon() {
                    return room_icon;
                }

                public void setRoom_icon(String room_icon) {
                    this.room_icon = room_icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<String> getHeader() {
                    return header;
                }

                public void setHeader(List<String> header) {
                    this.header = header;
                }
            }
        }

        public static class VoteBean {
            /**
             * name : 赞同。
             * vid : 4414
             * vote_num : 2770
             */

            private String name;
            private int vid;
            private int vote_num;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getVid() {
                return vid;
            }

            public void setVid(int vid) {
                this.vid = vid;
            }

            public int getVote_num() {
                return vote_num;
            }

            public void setVote_num(int vote_num) {
                this.vote_num = vote_num;
            }
        }
    }

    @Override
    public String toString() {
        return "NetAudioPagerData{" +
                "info=" + info +
                ", list=" + list +
                '}';
    }
}
