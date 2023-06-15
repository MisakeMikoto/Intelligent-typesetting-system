package com.hyc.nsms.utils;

import com.hyc.nsms.model.entity.Workload;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//工作量排序工具类
@Component
public class WorkloadSortUtils {
    //将所有护士和护工的工作量排序（白班）
    public static void morningWorkloadSort(List<Workload> workloadList) {
        Collections.sort(workloadList, new Comparator<Workload>() {
            public int compare(Workload arg0, Workload arg1) {
                //第一次比较白班次数（先排白班次数少的）
                int i = arg0.getCurrentMorningShifts().compareTo(arg1.getCurrentMorningShifts());
                //如果白班次数相同
                if (i == 0) {
                    //则进行上班天数比较（再排上班天数少的）
                    int j = arg0.getCurrentWorkDays().compareTo(arg1.getCurrentWorkDays());
                    //如果上班天数相同
                    if (j == 0) {
                        //则进行夜班天数比较（排夜班数量多的）
                        int k = arg1.getCurrentEveningShifts().compareTo(arg0.getCurrentEveningShifts());
                        //如果大夜班天数相同
                        if (k == 0) {
                            //则进行放假天数比较（排放假数量多的）
                            return arg1.getLeftDays().compareTo(arg0.getLeftDays());
                        }
                        return k;
                    }
                    return j;
                }
                return i;
            }
        });
    }

    //将所有护士和护工的工作量排序（小夜班）
    public static void middleWorkloadSort(List<Workload> workloadList) {
        Collections.sort(workloadList, new Comparator<Workload>() {
            public int compare(Workload arg0, Workload arg1) {
                //第一次比较中班次数（先排中班次数少的）
                int i = arg0.getCurrentMiddleShifts().compareTo(arg1.getCurrentMiddleShifts());
                //如果中班次数相同
                if (i == 0) {
                    //则进行上班天数比较（再排上班天数少的）
                    int j = arg0.getCurrentWorkDays().compareTo(arg1.getCurrentWorkDays());
                    //如果上班天数相同
                    if (j == 0) {
                        //则进行放假天数比较（排放假数量多的）
                        return arg1.getLeftDays().compareTo(arg0.getLeftDays());
                    }
                    return j;
                }
                return i;
            }
        });
    }

    //将所有护士和护工的工作量排序（大夜班）
    public static void nightWorkloadSort(List<Workload> workloadList){
        Collections.sort(workloadList, new Comparator<Workload>() {
            public int compare(Workload arg0, Workload arg1) {
                //第一次比较大夜班次数（先排大夜班次数少的）
                int i = arg0.getCurrentEveningShifts().compareTo(arg1.getCurrentEveningShifts());
                //如果大夜班次数相同
                if (i == 0) {
                    //则进行上班天数比较（再排上班数少的）
                    int j = arg0.getCurrentWorkDays().compareTo(arg1.getCurrentWorkDays());
                    //如果上班天数相同
                    if (j == 0) {
                        //则进行放假天数比较（排放假数量多的）
                        return arg1.getLeftDays().compareTo(arg0.getLeftDays());
                    }
                    return j;
                }
                return i;
            }
        });
    }
}
