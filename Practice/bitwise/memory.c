#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

/**
 * main - let's find out which syscall malloc is using
 *
 * Return: EXIT_FAILURE if something failed. Otherwise EXIT_SUCCESS
 */
int main(void)
{
    void *p;

    write(1, "BEFORE MALLOC\n", 14);
    p = malloc(1);
    write(1, "AFTER MALLOC\n", 13);
    printf("%p\n", p);
    getchar();
    return (EXIT_SUCCESS);
}


/*
aditya@pop-os:/proc/147432$ ls
arch_status  clear_refs       cwd      gid_map    maps        net        oom_score_adj  root       smaps         status         uid_map
attr         cmdline          environ  io         mem         ns         pagemap        sched      smaps_rollup  syscall        wchan
autogroup    comm             exe      limits     mountinfo   numa_maps  patch_state    schedstat  stack         task
auxv         coredump_filter  fd       loginuid   mounts      oom_adj    personality    sessionid  stat          timers
cgroup       cpuset           fdinfo   map_files  mountstats  oom_score  projid_map     setgroups  statm         timerslack_ns
*/

/*
Run strace 

*/

