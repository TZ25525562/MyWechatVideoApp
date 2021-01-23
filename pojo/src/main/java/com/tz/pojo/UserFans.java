package com.tz.pojo;

/**
 * 自定义的Users和UsersFans的pojo类，用于给前端返回数据
 */
public class UserFans {
    private Users users;
    private boolean isFollow;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
