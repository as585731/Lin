new Vue({
    el: '#app',
    data:{
        username:null,//用户名
        menuList:[]
    },
    created(){
        //发送请求获取当前登录用户的用户名
        axios.get('/user/getUsername.do').then((response)=>{
            this.username = response.data.data;
        });
    }
});