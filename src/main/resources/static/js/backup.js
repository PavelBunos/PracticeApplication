const api = window.axios;

Vue.component('database-row', {
    props: ['database'],
    template:
        '<option>' +
            '{{database.name}}' +
        '</option>'
});

Vue.component('databases-list', {
    props: ['databases'],
    template:
        '<select class="databases-list" v-model="selectedValue" @change="handleChange">' +
            '<database-row v-for="database in databases" :database="database" :key="database.databaseId" />' +
        '</select>',
    created: function() {
        api.get("/practiceApp/databases").then(response => {
            const data = response.data.data;
            data.forEach(database => {
                this.databases.push(database);
            })
        })
    },
    data() {
        return {
            selectedValue: null
        }
    },
    methods: {
        handleChange() {
            this.$emit('selected', this.selectedValue);
        }
    }
})

new Vue({
    el: '#app',
    data() {
        return {
            selectedDatabase: null,
            databases: [],
            backupArguments: [],
            backupPath: '',
            restoreArguments: [],
            restorePath: '',
            backupList: [],
            selectedBackup: '',
            tChecked: false,
            tArg: '-T p',

        };
    },
    methods: {
        startBackup() {
            if (this.backupPath.length > 0 && this.backupArguments) {
                let dumpData = {
                    path: this.backupPath,
                    dumpFileName: "",
                    args: this.backupArguments.join(' ') + ' ' + this.tArg,
                    database: this.selectedDatabase
                }

                api.post(`/practiceApp/dump/create`, dumpData).then(response => {
                    if (response.status == 200) {
                        alert("Бэкап успешно создан!");
                    } else {
                        alert(response.data.data);
                    }
                }).catch(error => {
                    switch (error.status) {
                        case 500:
                            alert("Не удалось создать - INTERNAL SERVER ERROR - 500");
                            break;
                        default:
                            alert("Непредвиденная ошибка!");
                            break;
                    }
                });
            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        updateBackupList() {
            if (this.restorePath) {
                api.get(`/practiceApp/dump/backups?path=${encodeURIComponent(this.restorePath)}`).then(response => {
                        if (response.status == 200) {
                            this.backupList = response.data.data;
                        } else {
                            alert(response.data.data);
                        }

                        console.log(response.data.data);
                    })
                    .catch(error => {
                        alert(error.response.data.data);
                    });
            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        startRestore() {
            if (this.restoreArguments && this.selectedBackup) {
                let restoreData = {
                    path: this.restorePath,
                    dumpFileName: this.selectedBackup,
                    args: this.restoreArguments.join(' '),
                    database: this.selectedDatabase
                }

                api.post(`/practiceApp/dump/restore`, restoreData).then(response => {
                    alert(response.data.data);
                }).catch(error => {
                    alert(error.response.data.data);
                });
            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        handleSelected(value) {
            this.selectedDatabase = value;
            console.log(this.selectedDatabase);
        }
    }
});