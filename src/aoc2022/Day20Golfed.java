package aoc2022;
class T{
String s(boolean p,String[]I){
java.util.List<long[]>a=(new java.util.ArrayList()),o=(new java.util.ArrayList());
for(var x:I){
long[]n={(new Long(x))*(p?1:811589153)};
a.add(n);o.add(n);
}
for(int r=p?1:10;r-->0;)for(var n:o){
int i=a.indexOf(n);
a.remove(i);
a.add((Math.floorMod(i+n[0],a.size())),n);
}
int z=0,i=4;
for(;a.get(z)[0]!=0;z++);
long r=0;for(;--i>0;)r+=a.get((z+i*1000)%a.size())[0];return""+r;
}
}
