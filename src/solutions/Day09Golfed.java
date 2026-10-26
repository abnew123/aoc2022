
class I{String s(boolean p,String[]I){int n=p?4:20,q[]=new int[n];var v=(new java.util.HashSet());v.add(0L);for(var l:I){int d=l.charAt(0),m=(new Integer(l.substring(2)));for(;m-->0;){q[d%6/3]+=d%4>0?1:-1;for(int i=2;i<n;i+=2){int a=q[i-2]-q[i],b=q[i-1]-q[i+1];if(a*a+b*b>2){q[i]+=(Integer.signum(a));q[i+1]+=(Integer.signum(b));}}v.add((long)q[n-2]<<32^q[n-1]);}}return""+v.size();}}
