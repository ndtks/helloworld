create table mail_header(
  id int,                         -- ID
  mail_from varchar(100),         -- FROMアドレス
  mail_bcc varchar(100),          -- BCCアドレス(カンマ区切りで複数の可能性あり)
  mail_cc varchar(100),           -- CCアドレス(カンマ区切りで複数の可能性あり)
  mail_to varchar(100),           -- TOアドレス(カンマ区切りで複数の可能性あり)
  mail_replyto varchar(100),      -- REPLYTOアドレス
  mail_subject varchar(100),      -- 件名
  mail_linecount int,             -- 行数(本文の？)
  mail_receiveddate varchar(100), -- 受信日
  mail_size int,                  -- サイズ
  mail_expunged char(1),          -- サーバから削除されたか否か
  mail_raw mediumtext,            -- メールヘッダーの生データ
  mail_contenttype varchar(100),  -- メールのContentType
  contenttype varchar(100),       -- PayloadのContentType
  mail_raw_md5 varchar(32),       -- mail_rawのMD5ハッシュ値
  inserted timestamp default current_timestamp, -- DBへの登録日時
  updated timestamp default current_timestamp,  -- DBへの更新日時
  primary key (id)
);

create table mail_body(
  id int,               -- ID
  body_text mediumtext, -- メール本文
  inserted timestamp default current_timestamp, -- DBへの登録日時
  updated timestamp default current_timestamp,  -- DBへの更新日時
  primary key (id)
);
