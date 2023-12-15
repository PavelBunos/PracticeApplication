var userApi = Vue.resource('/users')

Vue.component('user-row', {
    props: ['user'],
    template:
        '<tr>' +
            '<td>{{user.userid}}</td><td>{{user.username}}</td><td>{{user.password}}</td>' +
        '</tr>'
});

Vue.component('users-list', {
    props: ['users'],
    template:
        '<table>' +
            '<user-row v-for="user in users" :user="user" :key="user.userid"/>' +
        '</table>',
    created: function() {
        userApi.get().then(result =>
            result.json().then(data =>
                data.forEach(user => this.users.push(user))
            )
        )
    }
});

var app = new Vue({
    el: '#app',
    template: '<users-list :users="users" />',
    data: {
        users: []
    }
})