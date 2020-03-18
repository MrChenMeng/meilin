/*
 Navicat Premium Data Transfer

 Source Server         : sqlserver数据库
 Source Server Type    : SQL Server
 Source Server Version : 11002100
 Source Host           : localhost:1433
 Source Catalog        : MLSCADA
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 11002100
 File Encoding         : 65001

 Date: 11/03/2020 17:08:28
*/


-- ----------------------------
-- Table structure for ChanENG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ChanENG]') AND type IN ('U'))
	DROP TABLE [dbo].[ChanENG]
GO

CREATE TABLE [dbo].[ChanENG] (
  [LineName] varchar(50) COLLATE Chinese_PRC_CI_AS NULL,
  [Cdate] date NULL,
  [Ban] varchar(50) COLLATE Chinese_PRC_CI_AS NULL,
  [Value] nchar(10) COLLATE Chinese_PRC_CI_AS NULL
)
GO

ALTER TABLE [dbo].[ChanENG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for Comparative
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Comparative]') AND type IN ('U'))
	DROP TABLE [dbo].[Comparative]
GO

CREATE TABLE [dbo].[Comparative] (
  [number] float(53) NULL,
  [dc] nvarchar(255) COLLATE Chinese_PRC_CI_AS NULL,
  [name] nvarchar(255) COLLATE Chinese_PRC_CI_AS NULL
)
GO

ALTER TABLE [dbo].[Comparative] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ElecTab
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ElecTab]') AND type IN ('U'))
	DROP TABLE [dbo].[ElecTab]
GO

CREATE TABLE [dbo].[ElecTab] (
  [Exid] varchar(50) COLLATE Chinese_PRC_CI_AS NOT NULL,
  [LineName] varchar(50) COLLATE Chinese_PRC_CI_AS NULL,
  [IP] varchar(50) COLLATE Chinese_PRC_CI_AS NULL,
  [NO] varchar(50) COLLATE Chinese_PRC_CI_AS NULL,
  [LineId] int NULL
)
GO

ALTER TABLE [dbo].[ElecTab] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for Electric
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Electric]') AND type IN ('U'))
	DROP TABLE [dbo].[Electric]
GO

CREATE TABLE [dbo].[Electric] (
  [Etime] datetime NOT NULL,
  [EName] nvarchar(50) COLLATE Chinese_PRC_CI_AS NOT NULL,
  [ZONG] float(53) NULL,
  [JIAN] float(53) NULL,
  [FENG] float(53) NULL,
  [PING] float(53) NULL,
  [GU] float(53) NULL
)
GO

ALTER TABLE [dbo].[Electric] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ElectricDATA
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ElectricDATA]') AND type IN ('U'))
	DROP TABLE [dbo].[ElectricDATA]
GO

CREATE TABLE [dbo].[ElectricDATA] (
  [NID] int IDENTITY(1,1) NOT NULL,
  [Etime] datetime NULL,
  [EName] nvarchar(50) COLLATE Chinese_PRC_CI_AS NULL,
  [ZONG] float(53) NULL,
  [JIAN] float(53) NULL,
  [FENG] float(53) NULL,
  [PING] float(53) NULL,
  [GU] float(53) NULL,
  [OldZONG] float(53) NULL,
  [OldJIAN] float(53) NULL,
  [OldFENG] float(53) NULL,
  [OldPING] float(53) NULL,
  [OldGU] float(53) NULL
)
GO

ALTER TABLE [dbo].[ElectricDATA] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for Line
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Line]') AND type IN ('U'))
	DROP TABLE [dbo].[Line]
GO

CREATE TABLE [dbo].[Line] (
  [line_id] int NOT NULL,
  [avg_num] bigint NULL,
  [act_num] bigint NULL
)
GO

ALTER TABLE [dbo].[Line] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for TagValues
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[TagValues]') AND type IN ('U'))
	DROP TABLE [dbo].[TagValues]
GO

CREATE TABLE [dbo].[TagValues] (
  [id] bigint IDENTITY(1,1) NOT NULL,
  [number] bigint NOT NULL,
  [dt] datetime NOT NULL,
  [values1] real NULL,
  [values2] real NULL,
  [values3] real NULL,
  [values4] real NULL,
  [values5] real NULL,
  [values6] real NULL,
  [values7] real NULL,
  [values8] real NULL,
  [values9] real NULL,
  [values10] real NULL,
  [values11] real NULL,
  [values12] real NULL,
  [values13] real NULL,
  [values14] real NULL,
  [values15] real NULL,
  [values16] real NULL,
  [values17] real NULL,
  [values18] real NULL,
  [values19] real NULL,
  [values20] real NULL,
  [values21] real NULL,
  [values22] real NULL,
  [values23] real NULL,
  [values24] real NULL,
  [values25] real NULL,
  [values26] real NULL,
  [values27] real NULL,
  [values28] real NULL,
  [values29] real NULL,
  [values30] real NULL,
  [values31] real NULL,
  [values32] real NULL,
  [values33] real NULL,
  [values34] real NULL,
  [values35] real NULL,
  [values36] real NULL,
  [values37] real NULL,
  [values38] real NULL,
  [values39] real NULL,
  [values40] real NULL,
  [values41] real NULL,
  [values42] real NULL,
  [values43] real NULL,
  [values44] real NULL,
  [values45] real NULL,
  [values46] real NULL,
  [values47] real NULL,
  [values48] real NULL,
  [values49] real NULL,
  [values50] real NULL,
  [values51] real NULL,
  [values52] real NULL,
  [values53] real NULL,
  [values54] real NULL,
  [values55] real NULL,
  [values56] real NULL,
  [values57] real NULL,
  [values58] real NULL,
  [values59] real NULL,
  [values60] real NULL
)
GO

ALTER TABLE [dbo].[TagValues] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Procedure structure for U_PostExSave
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[U_PostExSave]') AND type IN ('P', 'PC', 'RF', 'X'))
	DROP PROCEDURE[dbo].[U_PostExSave]
GO

CREATE PROCEDURE [dbo].[U_PostExSave](@addr varchar(50),@value  float  )
AS
   BEGIN TRANSACTION
   BEGIN
 
    Declare @Zong float,@hour int 
	
	set @hour=-1   
   select @Zong=Zong ,@hour= DATENAME(HOUR, Etime ) from Electric where Ename =@addr  

 if  DATENAME(HOUR, getdate()) <> @hour
 begin
   insert into ElectricDATA (Etime,Ename,Zong) values (getdate(),@addr,@value-@Zong)

   delete from Electric where Ename =@addr
    insert into Electric(Etime,Ename,Zong ) values (getdate(),@addr,@value)

  end

	 


   IF @@error<>0 rollback 
   ELSE commit


   END
GO


-- ----------------------------
-- Procedure structure for Board
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[Board]') AND type IN ('P', 'PC', 'RF', 'X'))
	DROP PROCEDURE[dbo].[Board]
GO

CREATE PROCEDURE [dbo].[Board]
 (  @Index  int  ,@Para varchar(50) ,@para2 varchar(50) ,@para3 varchar(50) , @para4 varchar(50) )
as
begin
    if @index=0  ----主看板不良分布饼图
	begin
	    
		select  '塑化不良' as name  ,23 as value 
		union 
		select  '小焦子' as name  ,60 as value 
		union 
		select  '拉伸强度' as name  ,12 as value 
		union 
		select  '断裂' as name  ,11 as value 
		union 
		select  '电阻率' as name  ,55 as value 
		union 
		select  '热变形' as name  ,90 as value 
 

     end
	  if @index=1  ----主看板能耗
	begin
	    
		select  '2019-10-14' as name  ,19873 as value 
		union 
		select  '2019-10-13' as name  ,21098 as value 
		union 
		select  '2019-10-12' as name  ,20343 as value 
		union 
		select  '2019-10-11' as name  ,21487 as value 
		union 
		select  '2019-10-10' as name  ,21067 as value 
		union 
		select  '2019-10-09' as name  ,20634 as value 
 		union 
		select  '2019-10-08' as name  ,21099 as value 
 		union 
		select  '2019-10-07' as name  ,19762 as value 
     end

	 if @index=2 
	 begin
	     
           select top 8 Cdate,sum(convert(float,value)) value from ChanEng group by Cdate   order by Cdate desc 
	    
		
	 end



end
GO


-- ----------------------------
-- Function structure for inttohex
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[inttohex]') AND type IN ('FN', 'FS', 'FT', 'IF', 'TF'))
	DROP FUNCTION[dbo].[inttohex]
GO

CREATE FUNCTION [dbo].[inttohex](@i int) 
returns varchar(15)  
begin 
--declare @i int 
--set @i=11259375 
declare @r varchar(10)   
set @r='' 
while @i/16>0 
begin 
set @r= 
(case  
when (@i % 16)<=9 then convert(varchar(1),@i % 16) 
when (@i % 16)=10 then 'A' 
when (@i % 16)=11 then 'B' 
when (@i % 16)=12 then 'C' 
when (@i % 16)=13 then 'D' 
when (@i % 16)=14 then 'E' 
when (@i % 16)=15 then 'F' 
end) 
+@r 
--select @r,@i 
set @i=@i/16 
end 
--select @r,@i 
if @i>0  
set @r=(case  
when (@i % 16)<=9 then convert(varchar(1),@i % 16) 
when (@i % 16)=10 then 'A' 
when (@i % 16)=11 then 'B' 
when (@i % 16)=12 then 'C' 
when (@i % 16)=13 then 'D' 
when (@i % 16)=14 then 'E' 
when (@i % 16)=15 then 'F' 
end)+@r 
-- select @r 
return  '0x000000'+ @r 
end
GO


-- ----------------------------
-- Function structure for urlencode
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[urlencode]') AND type IN ('FN', 'FS', 'FT', 'IF', 'TF'))
	DROP FUNCTION[dbo].[urlencode]
GO

CREATE FUNCTION [dbo].[urlencode](@v   varchar(max))   returns   varchar(max) 
as 
begin 
    declare   @s   varchar(max)     
    set   @s   =   '' 

	 while   len(@v)   >   0 
    begin	  
	  set   @s=@s+'%'+ right(dbo.inttohex(Unicode(substring(@v,1,1))),4)	
	  set   @v   =   substring(@v,   2,   len(@v)   -  1) 	  	
    end

    return(@s) 
end
GO


-- ----------------------------
-- Procedure structure for SendSmS
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SendSmS]') AND type IN ('P', 'PC', 'RF', 'X'))
	DROP PROCEDURE[dbo].[SendSmS]
GO

CREATE PROCEDURE [dbo].[SendSmS]  @serder varchar(100),@serdee varchar(100),@title varchar(500),@content varchar(5000)
as
begin
-----exec SendSmS 'INDUSFO' ,'@all','推送','恩大消息推送，从今天起推送车间设备异常消息，请注意查收 '
declare @err int,@src varchar(255),@desc varchar(255)
declare @obj int,@urlstr nvarchar(4000),@function varchar(4)
set @urlstr= 'http://192.168.0.200:9981/WE/POST?title='+dbo.urlencode(@title)+'&content='+dbo.urlencode(@content)+'&sender='+dbo.urlencode(@serder)+'&sendee='+@serdee+'&agentid=1000004'
--print @urlstr

-- 创建 xmlhttp 对象

exec @err=sp_oacreate 'MICROSOFT.XMLHTTP',@obj out
if @err<>0 goto lberr
 
-- open 
exec @err=sp_oamethod @obj,'open',null, 'POST', @urlstr, 0
if @err<>0 goto lberr
 
-- send
exec @err=sp_oamethod @obj,'send',null,''
if @err<>0 goto lberr
 
-- 释放 xmlhttp 对象
exec @err=sp_oadestroy @obj
return
 
lberr:
    exec sp_oageterrorinfo 0,@src out,@desc out
    select cast(@err as varbinary(4)) as 错误号
        ,@src as 错误源,@desc as 错误描述  
end
GO


-- ----------------------------
-- Procedure structure for SendelEctric
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SendelEctric]') AND type IN ('P', 'PC', 'RF', 'X'))
	DROP PROCEDURE[dbo].[SendelEctric]
GO

CREATE PROCEDURE [dbo].[SendelEctric]
	 
AS
BEGIN
 SET ANSI_WARNINGS OFF

 declare @v varchar(1000)

 declare @x float 

 declare @a float
 declare @b float
 declare @c float
 declare @d float
 declare @e float
 declare @f float
 declare @g float

 select @x=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23)

 select @a=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23) and EName=001805176764  --1#

 select @b=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23) and EName=001805176763  --2#

 select @c=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23) and EName=001805176766  --2#

 select @d=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23) and EName=001805176765  --3#

 select @e=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23) and EName=001805176768  --4#

 select @f=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23) and EName=001805176762  --5#

 select @g=isnull(sum(zong),0) from ElectricDATA  where  convert(varchar(10),Etime,120)=CONVERT(VARCHAR,GETDATE()-1,23) and EName=001805176770  --6#


  
 if @x=0
 begin 
 set @v='昨日无数据'
 end

 else
 begin 
 set @v= +char(10)+ 
 '昨日产线总能耗'+convert(varchar(10),@x)+'度电' 
 +char(10)+ 
 +char(10)+ 
 '1号线能耗为'+convert(varchar(10),@a)+'度电'  
 +char(10)+ 
 '2号线能耗为'+convert(varchar(10),@b+@c)+'度电'
 +char(10)+ 
 '3号线能耗为'+convert(varchar(10),@d)+'度电'
 +char(10)+ 
 '4号线能耗为'+convert(varchar(10),@e)+'度电'
 +char(10)+ 
 '5号线能耗为'+convert(varchar(10),@f)+'度电'
 +char(10)+ 
 '6号线能耗为'+convert(varchar(10),@g)+'度电'
 +char(10)+ 
 +char(10)+ 
 '数据由系统自动采集' 
 +char(10)+  
 '请相关人员知悉'
 end


  exec [dbo].[SendSmS] 'INDUSFO' ,'QianHuan','美临产线总能耗推送',@V
  exec [dbo].[SendSmS] 'INDUSFO' ,'PanSuWen','美临产线总能耗推送',@V  --王潇
  exec [dbo].[SendSmS] 'INDUSFO' ,'QianMeiLing','美临产线总能耗推送',@V  --钱美玲
  exec [dbo].[SendSmS] 'INDUSFO' ,'ShuZhiYingXiaoRanMoEr','美临产线总能耗推送',@V  --舒工
  exec [dbo].[SendSmS] 'INDUSFO' ,'DaoKuanZhiYuan','美临产线总能耗推送',@V  --姜工
  exec [dbo].[SendSmS] 'INDUSFO' ,'XiaoPing','美临产线总能耗推送',@V  --钱厂
  exec [dbo].[SendSmS] 'INDUSFO' ,'GuoFu','美临产线总能耗推送',@V  --刘国富
   
END 

--  exec  SendelEctric
--  +char(13)+
--  CONVERT(VARCHAR,GETDATE()-1,23)
--  SET ANSI_WARNINGS OFF
GO


-- ----------------------------
-- Procedure structure for p_post
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[p_post]') AND type IN ('P', 'PC', 'RF', 'X'))
	DROP PROCEDURE[dbo].[p_post]
GO

CREATE PROCEDURE [dbo].[p_post](
	@nid varchar(50)
)
AS
BEGIN
declare @ServiceUrl as varchar(1000)

PRINT 'http://192.168.0.200:8080/product/weChatEnter/warm?id='+@nid ---触发触发时传过来的参数
set @ServiceUrl='http://192.168.0.200:8080/product/weChatEnter/warm?id='+@nid

  Declare @Object as Int
  Declare @ResponseText as Varchar(8000)
  Exec sp_OACreate'MSXML2.XMLHTTP',@Object OUT;
  Exec sp_OAMethod @Object, 'open',NULL,'get',@ServiceUrl,'false' 
  Exec sp_OAMethod @Object,'send' 
  Exec sp_OAMethod @Object,'responseText',@ResponseText OUTPUT 

Select @ResponseText
Exec sp_OADestroy @Object


END
GO


-- ----------------------------
-- Primary Key structure for table ElecTab
-- ----------------------------
ALTER TABLE [dbo].[ElecTab] ADD CONSTRAINT [PK_ElecTab] PRIMARY KEY CLUSTERED ([Exid])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table Electric
-- ----------------------------
ALTER TABLE [dbo].[Electric] ADD CONSTRAINT [PK_Electric] PRIMARY KEY CLUSTERED ([Etime], [EName])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ElectricDATA
-- ----------------------------
ALTER TABLE [dbo].[ElectricDATA] ADD CONSTRAINT [PK_ElectricDATA] PRIMARY KEY CLUSTERED ([NID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table Line
-- ----------------------------
ALTER TABLE [dbo].[Line] ADD CONSTRAINT [PK__Line__F5AE5F62B81D40FB] PRIMARY KEY CLUSTERED ([line_id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = OFF, ALLOW_PAGE_LOCKS = OFF)  
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table TagValues
-- ----------------------------
ALTER TABLE [dbo].[TagValues] ADD CONSTRAINT [PK_TagValues] PRIMARY KEY CLUSTERED ([id])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)  
ON [PRIMARY]
GO

