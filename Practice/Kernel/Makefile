#obj-m   := kerne_panic.o 
obj-m += Kernel_2_Panic.o 
#test-module.o
all:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) -Wno-strict-prototypes modules

clean:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) clean
