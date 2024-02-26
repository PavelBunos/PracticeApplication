const axiosInstance = window.axios;

Vue.component('database-row', {
    props: ['database', 'selectedDatabase'],
    template:
        '<tr :class="rowClasses" :isSelected="database === selectedDatabase" @click="selectDatabase(database)">' +
            '<td>{{database.name}}</td>' +
        '</tr>',
    computed: {
        rowClasses() {
            return {
                'selected-row': this.isSelected,
                'unelected-row': !this.isSelected
            };
        }
    },
    methods: {
        selectDatabase(database) {
            this.selectedDatabase = database;
            console.log(this.selectedDatabase)
        }
    }
});

Vue.component('databases-list', {
    props: ['databases'],
    template:
        '<table class="databases-list">' +
            '<database-row v-for="database in databases" :database="database" :key="database.databaseId" />' +
        '</table>',
    created: function() {
        axiosInstance.get("/practiceApp/databases").then(result => {
            const data = result.data;
            data.forEach(database => {
                this.databases.push(database);
            })
        })
    },
    data() {
        return {
            selectedDatabase: null
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
            tArg: '-T p'
        };
    },
    methods: {
        startBackup() {
            if (this.backupPath.length > 0 && this.backupArguments) {
                console.log('Начато копирование с аргументами:', this.backupArguments);
                console.log('Путь сохранения:', this.backupPath);

                let dumpData = {
                    path: this.backupPath,
                    dumpFileName: "",
                    args: this.backupArguments.join(' ') + ' ' + this.tArg
                }

                axios.post(`/practiceApp/dump/create`, dumpData).then(function (response) {
                    alert(response.data.message);
                });

            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        updateBackupList() {
            if (this.restorePath) {
                console.log('Обновлен список бэкапов из пути:', this.restorePath);
                axios.get(`/practiceApp/dump/backups?path=${encodeURIComponent(this.restorePath)}`)
                    .then(response => {
                        this.backupList = response.data;
                    })
                    .catch(error => {
                        console.error('Ошибка при получении списка бэкапов:', error);
                    });
            } else {
                alert('Заполните все необходимые поля!');
            }
        },
        startRestore() {
            if (this.restoreArguments && this.selectedBackup) {
                console.log('Запущено восстановление с аргументами:', this.restoreArguments);
                console.log('Выбранный бэкап:', this.selectedBackup);

                let restoreData = {
                    path: this.restorePath,
                    dumpFileName: this.selectedBackup,
                    args: this.restoreArguments.join(' ')
                }

                axios.post(`/practiceApp/dump/restore/to`, restoreData).then(function (response) {
                    alert(response.data.message);
                });
            } else {
                alert('Заполните все необходимые поля!');
            }
        }
    }
});