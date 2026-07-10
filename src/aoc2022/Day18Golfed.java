package aoc2022;
class R{
String s(boolean p,String I){
var a=new int[25][25][25];
for(var l:I.split("\n")){
var w=l.split(",");
a[$.i(w[0])+1][$.i(w[1])+1][$.i(w[2])+1]=1;
}
int[]d={1,-1,25,-25,625,-625},q=new int[20000];a[0][0][0]=2;
for(int h=0,t=1;h<t;h++){
int v=q[h],x=v/625,y=v/25%25,z=v%25;
for(int u:d){
int n=v+u,X=n/625,Y=n/25%25,Z=n%25;
if(n>=0&&n<15625&&$.a(x-X)+$.a(y-Y)+$.a(z-Z)==1&&a[X][Y][Z]<1){a[X][Y][Z]=2;q[t++]=n;}
}
}
int r=0;
for(int x=0;x<25;x++)for(int y=0;y<25;y++)for(int z=0;z<25;z++)if(a[x][y][z]==1)for(int u:d){
int n=x*625+y*25+z+u,X=n/625,Y=n/25%25,Z=n%25;
var o=n>=0&&n<15625&&$.a(x-X)+$.a(y-Y)+$.a(z-Z)==1;
if(p?!o||a[X][Y][Z]!=1:o&&a[X][Y][Z]==2)r++;
}
return""+r;
}
}
