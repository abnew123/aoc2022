package aoc2022;
class Q{
int P[]={15,132866,263175,16843009,771},h;byte c[];
String s(boolean p,String[]j){
c=new byte[1];h=0;var J=j[0];var m=$.h();long n=p?2022:0xe8d4a51000L,r=0,e=0;int q=0;
for(;r<n;r++){
int k=$.f(r,5),x=2,y=h+3,X;
for(;;){
X=x+J.charAt(q)-61;q=++q%J.length();
if(o(P[k],X,y)>0)x=X;
if(o(P[k],x,y-1)>0)y--;else{for(int d=P[k],w;d>0;d>>=8,y++){if(y==c.length)c=java.util.Arrays.copyOf(c,y*2);w=d%256<<x;c[y]|=w;h=$.x(h,y+1);}break;}
}
if(e<1){
int f=h,i=7,z;
for(;i-->0;)for(z=h;z-->0;)if((c[z]&1<<i)>0){f=$.n(f,z+1);break;}
var v=(long[])m.put((r+1)%5+","+q+":"+new String(c,0,f,h-f),new long[]{r+1,h});
if(v!=null){long a=r+1-v[0],g=(n-r-1)/a;r+=g*a;e=g*(h-v[1]);}
}
}
return e+h+"";
}
int o(int p,int x,int y){
if((x|y)<0)return 0;
for(;p>0;p>>=8,y++){
int w=p%256<<x;
if(w>127||y<c.length&&(w&c[y])>0)return 0;
}
return 1;
}
}
