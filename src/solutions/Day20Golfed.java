
class T{
String s(boolean p,String[]I){
var a=new java.util.Stack<long[]>();
for(var x:I)a.add(new long[]{new Long(x)*(p?1:811589153)});
var o=a.toArray(new long[0][]);
for(int r=p?1:10;r-->0;)for(var n:o){
int i=a.indexOf(n);
a.add(Math.floorMod(i+n[0],a.size()-1),a.remove(i));
}
int z=0,i=4;
for(;a.get(z)[0]!=0;z++);
long r=0;for(;--i>0;)r+=a.get((z+i*1000)%a.size())[0];return""+r;
}
}
