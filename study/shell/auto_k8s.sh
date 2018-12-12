#!/bin/bash

`systemctl  stop  firewalld&&systemctl  disable   firewalld`

`swapoff  -a && echo "vm.swappiness = 0">> /etc/sysctl.conf`


`sed -i "s/^SELINUX=enforcing/SELINUX=disabled/g" /etc/sysconfig/selinux`


`cat <<EOF>>/etc/sysctl.conf
net.bridge.bridge-nf-call-iptables = 1
 
net.bridge.bridge-nf-call-ip6tables = 1
 
EOF
`

#party mobile
`modprobe br_netfilter`



sysctl -p





`cat <<EOF > /etc/yum.repos.d/kubernetes.repo
 
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
 
EOF`




yum -y install epel-release


yum clean all

yum makecache


yum -y install docker kubelet kubeadm kubectl kubernetes-cni



`cat <<EOF >/etc/docker/daemon.json
 
 {

"registry-mirrors": ["https://65156067.mirror.aliyuncs.com"],

"dns": ["192.168.32.2"]

}

EOF`


`sed -i "s/--selinux-enabled/--selinux-enabled=false/g" /etc/sysconfig/docker`
`systemctl daemon-reload&&systemctl enable docker && systemctl start docker`
 
 

images=(kube-proxy-amd64:v1.10.0 kube-scheduler-amd64:v1.10.0 kube-controller-manager-amd64:v1.10.0 kube-apiserver-amd64:v1.10.0
etcd-amd64:3.1.12 pause-amd64:3.1 kubernetes-dashboard-amd64:v1.8.3 k8s-dns-sidecar-amd64:1.14.8 k8s-dns-kube-dns-amd64:1.14.8
k8s-dns-dnsmasq-nanny-amd64:1.14.8)
for imageName in ${images[@]} ; do
  docker pull keveon/$imageName
  docker tag keveon/$imageName k8s.gcr.io/$imageName
  docker rmi keveon/$imageName
done


kubeadm init --kubernetes-version=v1.10.0 --pod-network-cidr=10.244.0.0/16 >/tmp/auto_kubelt

`cat /tmp/auto_kubelt |grep 'kubeadm join' >/tmp/token`


`echo "export KUBECONFIG=/etc/kubernetes/admin.conf" >> ~/.bash_profile`

source ~/.bash_profile



`mkdir -p /etc/cni/net.d/`


`cat <<EOF> /etc/cni/net.d/10-flannel.conf
{
“name”: “cbr0”,
“type”: “flannel”,
“delegate”: {
“isDefaultGateway”: true
}
}
 
EOF`


`mkdir /usr/share/oci-umount/oci-umount.d -p`


`mkdir /run/flannel/`


`cat <<EOF> /run/flannel/subnet.env
FLANNEL_NETWORK=10.244.0.0/16
FLANNEL_SUBNET=10.244.1.0/24
FLANNEL_MTU=1450
FLANNEL_IPMASQ=true
 
EOF`



kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/v0.9.1/Documentation/kube-flannel.yml



systemctl  start  kubelet 
 



#在node1和node2节点上分别执行kubeadm join命令  /tmp/token


#http://blog.51cto.com/devingeng/2096495






















