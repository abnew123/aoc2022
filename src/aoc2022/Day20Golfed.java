package aoc2022;
class T{
String s(boolean p,String[]I){
java.util.List<long[]>a=$.a(),o=$.a();
for(var x:I){
long[]n={$.l(x)*(p?1:811589153)};
a.add(n);o.add(n);
}
for(int r=p?1:10;r-->0;)for(var n:o){
int i=a.indexOf(n);
a.remove(i);
a.add($.f(i+n[0],a.size()),n);
}
int z=0,i=4;
for(;a.get(z)[0]!=0;z++);
long r=0;for(;--i>0;)r+=a.get((z+i*1000)%a.size())[0];return""+r;
}
}
