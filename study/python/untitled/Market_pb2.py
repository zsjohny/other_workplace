# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Market.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf.internal import enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='Market.proto',
  package='',
  syntax='proto2',
  serialized_pb=_b('\n\x0cMarket.proto\"\xfb\x03\n\x08Response\x12\'\n\x0eidentification\x18\x01 \x01(\x0b\x32\x0f.Identification\x12\x1d\n\theartbeat\x18\x02 \x01(\x0b\x32\n.Heartbeat\x12\x1d\n\tquoteDyna\x18\x03 \x01(\x0b\x32\n.QuoteDyna\x12\'\n\x0equoteFiveRange\x18\x04 \x01(\x0b\x32\x0f.QuoteFiveRange\x12\x1b\n\x08quoteMin\x18\x05 \x03(\x0b\x32\t.QuoteMin\x12\x1d\n\x08klineDay\x18\x06 \x03(\x0b\x32\x0b.QuoteKline\x12\x1e\n\tklineWeek\x18\x07 \x03(\x0b\x32\x0b.QuoteKline\x12\x1f\n\nklineMonth\x18\x08 \x03(\x0b\x32\x0b.QuoteKline\x12\x1e\n\tklineMin1\x18\t \x03(\x0b\x32\x0b.QuoteKline\x12\x1e\n\tklineMin5\x18\n \x03(\x0b\x32\x0b.QuoteKline\x12\x1f\n\nklineMin15\x18\x0b \x03(\x0b\x32\x0b.QuoteKline\x12\x1f\n\nklineMin30\x18\x0c \x03(\x0b\x32\x0b.QuoteKline\x12\x1f\n\nklineMin60\x18\r \x03(\x0b\x32\x0b.QuoteKline\x12 \n\x0bklineMin240\x18\x0e \x03(\x0b\x32\x0b.QuoteKline\x12\x1d\n\tquoteInst\x18\x0f \x01(\x0b\x32\n.QuoteInst\"Y\n\x0eIdentification\x12\x1b\n\x08\x63\x61tegory\x18\x01 \x01(\x0e\x32\t.Category\x12\x1b\n\x08instType\x18\x02 \x01(\x0e\x32\t.InstType\x12\r\n\x05login\x18\x03 \x01(\x04\"\x19\n\tHeartbeat\x12\x0c\n\x04info\x18\x01 \x01(\t\"\xce\x01\n\tQuoteDyna\x12\x0c\n\x04time\x18\x01 \x01(\t\x12\x12\n\nopen_price\x18\x02 \x01(\t\x12\x0f\n\x07up_down\x18\x03 \x01(\t\x12\x14\n\x0cup_down_rate\x18\x04 \x01(\t\x12\x15\n\rhighest_price\x18\x05 \x01(\t\x12\x14\n\x0clowest_price\x18\x06 \x01(\t\x12\x12\n\nlast_price\x18\x07 \x01(\t\x12\x12\n\nlast_close\x18\x08 \x01(\t\x12\x13\n\x0blast_settle\x18\t \x01(\t\x12\x0e\n\x06volume\x18\n \x01(\x03\"\xc9\x03\n\x0eQuoteFiveRange\x12\x12\n\nbuy_price1\x18\x01 \x01(\t\x12\x12\n\nbuy_price2\x18\x02 \x01(\t\x12\x12\n\nbuy_price3\x18\x03 \x01(\t\x12\x12\n\nbuy_price4\x18\x04 \x01(\t\x12\x12\n\nbuy_price5\x18\x05 \x01(\t\x12\x13\n\x0b\x62uy_volume1\x18\x06 \x01(\x03\x12\x13\n\x0b\x62uy_volume2\x18\x07 \x01(\x03\x12\x13\n\x0b\x62uy_volume3\x18\x08 \x01(\x03\x12\x13\n\x0b\x62uy_volume4\x18\t \x01(\x03\x12\x13\n\x0b\x62uy_volume5\x18\n \x01(\x03\x12\x13\n\x0bsell_price1\x18\x0b \x01(\t\x12\x13\n\x0bsell_price2\x18\x0c \x01(\t\x12\x13\n\x0bsell_price3\x18\r \x01(\t\x12\x13\n\x0bsell_price4\x18\x0e \x01(\t\x12\x13\n\x0bsell_price5\x18\x0f \x01(\t\x12\x14\n\x0csell_polume1\x18\x10 \x01(\x03\x12\x14\n\x0csell_polume2\x18\x11 \x01(\x03\x12\x14\n\x0csell_polume3\x18\x12 \x01(\x03\x12\x14\n\x0csell_polume4\x18\x13 \x01(\x03\x12\x14\n\x0csell_polume5\x18\x14 \x01(\x03\x12\x13\n\x0blast_settle\x18\x15 \x01(\t\"~\n\x08QuoteMin\x12\n\n\x02id\x18\x01 \x03(\x04\x12\x0c\n\x04time\x18\x02 \x03(\t\x12\r\n\x05price\x18\x03 \x03(\t\x12\x0e\n\x06volume\x18\x04 \x03(\x03\x12\x0e\n\x06\x61mount\x18\x05 \x03(\t\x12\x14\n\x0cup_down_rate\x18\x06 \x03(\t\x12\x13\n\x0blast_settle\x18\x07 \x03(\t\"~\n\nQuoteKline\x12\n\n\x02id\x18\x01 \x03(\x04\x12\x0c\n\x04time\x18\x02 \x03(\t\x12\x0c\n\x04high\x18\x03 \x03(\t\x12\x0c\n\x04open\x18\x04 \x03(\t\x12\x0b\n\x03low\x18\x05 \x03(\t\x12\r\n\x05\x63lose\x18\x06 \x03(\t\x12\x0e\n\x06volume\x18\x07 \x03(\x03\x12\x0e\n\x06\x61mount\x18\x08 \x03(\t\"7\n\tQuoteInst\x12\x15\n\x05state\x18\x01 \x01(\x0e\x32\x06.State\x12\x13\n\x0b\x64\x65scription\x18\x02 \x01(\t*\xc8\x03\n\x08\x43\x61tegory\x12\r\n\tHEARTBEAT\x10\x00\x12\r\n\tQUOTEDYNA\x10\x01\x12\x12\n\x0eQUOTEFIVERANGE\x10\x02\x12\x0c\n\x08QUOTEMIN\x10\x03\x12\x0c\n\x08KLINEDAY\x10\x04\x12\r\n\tKLINEWEEK\x10\x05\x12\x0e\n\nKLINEMONTH\x10\x06\x12\r\n\tKLINEMIN1\x10\x07\x12\r\n\tKLINEMIN5\x10\x08\x12\x0e\n\nKLINEMIN15\x10\t\x12\x0e\n\nKLINEMIN30\x10\n\x12\x0e\n\nKLINEMIN60\x10\x0b\x12\x0f\n\x0bKLINEMIN240\x10\x0c\x12\r\n\tQUOTEINST\x10\r\x12\x10\n\x0c\x41LLINSTQUOTE\x10\x0e\x12\r\n\tORDERPAGE\x10\x0f\x12\x10\n\x0cQUOTEMINPAGE\x10\x10\x12\x10\n\x0cKLINEDAYPAGE\x10\x11\x12\x11\n\rKLINEWEEKPAGE\x10\x12\x12\x12\n\x0eKLINEMONTHPAGE\x10\x13\x12\x11\n\rKLINEMIN1PAGE\x10\x14\x12\x11\n\rKLINEMIN5PAGE\x10\x15\x12\x12\n\x0eKLINEMIN15PAGE\x10\x16\x12\x12\n\x0eKLINEMIN30PAGE\x10\x17\x12\x12\n\x0eKLINEMIN60PAGE\x10\x18\x12\x13\n\x0fKLINEMIN240PAGE\x10\x19*,\n\x08InstType\x12\t\n\x05\x41U_TD\x10\x00\x12\t\n\x05\x41G_TD\x10\x01\x12\n\n\x06MAU_TD\x10\x02*[\n\x05State\x12\x11\n\rTRADE_INVALID\x10\x00\x12\x0f\n\x0bTRADE_START\x10\x01\x12\r\n\tTRADE_ING\x10\x02\x12\x0f\n\x0bTRADE_CLOSE\x10\x03\x12\x0e\n\nTRADE_REST\x10\x04\x42-\n#com.goldplusgold.td.market.protobufB\x06Market')
)
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

_CATEGORY = _descriptor.EnumDescriptor(
  name='Category',
  full_name='Category',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='HEARTBEAT', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='QUOTEDYNA', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='QUOTEFIVERANGE', index=2, number=2,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='QUOTEMIN', index=3, number=3,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEDAY', index=4, number=4,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEWEEK', index=5, number=5,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMONTH', index=6, number=6,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN1', index=7, number=7,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN5', index=8, number=8,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN15', index=9, number=9,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN30', index=10, number=10,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN60', index=11, number=11,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN240', index=12, number=12,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='QUOTEINST', index=13, number=13,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='ALLINSTQUOTE', index=14, number=14,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='ORDERPAGE', index=15, number=15,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='QUOTEMINPAGE', index=16, number=16,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEDAYPAGE', index=17, number=17,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEWEEKPAGE', index=18, number=18,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMONTHPAGE', index=19, number=19,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN1PAGE', index=20, number=20,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN5PAGE', index=21, number=21,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN15PAGE', index=22, number=22,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN30PAGE', index=23, number=23,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN60PAGE', index=24, number=24,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='KLINEMIN240PAGE', index=25, number=25,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=1627,
  serialized_end=2083,
)
_sym_db.RegisterEnumDescriptor(_CATEGORY)

Category = enum_type_wrapper.EnumTypeWrapper(_CATEGORY)
_INSTTYPE = _descriptor.EnumDescriptor(
  name='InstType',
  full_name='InstType',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='AU_TD', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='AG_TD', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='MAU_TD', index=2, number=2,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=2085,
  serialized_end=2129,
)
_sym_db.RegisterEnumDescriptor(_INSTTYPE)

InstType = enum_type_wrapper.EnumTypeWrapper(_INSTTYPE)
_STATE = _descriptor.EnumDescriptor(
  name='State',
  full_name='State',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='TRADE_INVALID', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='TRADE_START', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='TRADE_ING', index=2, number=2,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='TRADE_CLOSE', index=3, number=3,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='TRADE_REST', index=4, number=4,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=2131,
  serialized_end=2222,
)
_sym_db.RegisterEnumDescriptor(_STATE)

State = enum_type_wrapper.EnumTypeWrapper(_STATE)
HEARTBEAT = 0
QUOTEDYNA = 1
QUOTEFIVERANGE = 2
QUOTEMIN = 3
KLINEDAY = 4
KLINEWEEK = 5
KLINEMONTH = 6
KLINEMIN1 = 7
KLINEMIN5 = 8
KLINEMIN15 = 9
KLINEMIN30 = 10
KLINEMIN60 = 11
KLINEMIN240 = 12
QUOTEINST = 13
ALLINSTQUOTE = 14
ORDERPAGE = 15
QUOTEMINPAGE = 16
KLINEDAYPAGE = 17
KLINEWEEKPAGE = 18
KLINEMONTHPAGE = 19
KLINEMIN1PAGE = 20
KLINEMIN5PAGE = 21
KLINEMIN15PAGE = 22
KLINEMIN30PAGE = 23
KLINEMIN60PAGE = 24
KLINEMIN240PAGE = 25
AU_TD = 0
AG_TD = 1
MAU_TD = 2
TRADE_INVALID = 0
TRADE_START = 1
TRADE_ING = 2
TRADE_CLOSE = 3
TRADE_REST = 4



_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='identification', full_name='Response.identification', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='heartbeat', full_name='Response.heartbeat', index=1,
      number=2, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='quoteDyna', full_name='Response.quoteDyna', index=2,
      number=3, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='quoteFiveRange', full_name='Response.quoteFiveRange', index=3,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='quoteMin', full_name='Response.quoteMin', index=4,
      number=5, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineDay', full_name='Response.klineDay', index=5,
      number=6, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineWeek', full_name='Response.klineWeek', index=6,
      number=7, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineMonth', full_name='Response.klineMonth', index=7,
      number=8, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineMin1', full_name='Response.klineMin1', index=8,
      number=9, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineMin5', full_name='Response.klineMin5', index=9,
      number=10, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineMin15', full_name='Response.klineMin15', index=10,
      number=11, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineMin30', full_name='Response.klineMin30', index=11,
      number=12, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineMin60', full_name='Response.klineMin60', index=12,
      number=13, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='klineMin240', full_name='Response.klineMin240', index=13,
      number=14, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='quoteInst', full_name='Response.quoteInst', index=14,
      number=15, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=17,
  serialized_end=524,
)


_IDENTIFICATION = _descriptor.Descriptor(
  name='Identification',
  full_name='Identification',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='category', full_name='Identification.category', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='instType', full_name='Identification.instType', index=1,
      number=2, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='login', full_name='Identification.login', index=2,
      number=3, type=4, cpp_type=4, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=526,
  serialized_end=615,
)


_HEARTBEAT = _descriptor.Descriptor(
  name='Heartbeat',
  full_name='Heartbeat',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='info', full_name='Heartbeat.info', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=617,
  serialized_end=642,
)


_QUOTEDYNA = _descriptor.Descriptor(
  name='QuoteDyna',
  full_name='QuoteDyna',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='time', full_name='QuoteDyna.time', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='open_price', full_name='QuoteDyna.open_price', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='up_down', full_name='QuoteDyna.up_down', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='up_down_rate', full_name='QuoteDyna.up_down_rate', index=3,
      number=4, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='highest_price', full_name='QuoteDyna.highest_price', index=4,
      number=5, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='lowest_price', full_name='QuoteDyna.lowest_price', index=5,
      number=6, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='last_price', full_name='QuoteDyna.last_price', index=6,
      number=7, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='last_close', full_name='QuoteDyna.last_close', index=7,
      number=8, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='last_settle', full_name='QuoteDyna.last_settle', index=8,
      number=9, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='volume', full_name='QuoteDyna.volume', index=9,
      number=10, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=645,
  serialized_end=851,
)


_QUOTEFIVERANGE = _descriptor.Descriptor(
  name='QuoteFiveRange',
  full_name='QuoteFiveRange',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='buy_price1', full_name='QuoteFiveRange.buy_price1', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_price2', full_name='QuoteFiveRange.buy_price2', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_price3', full_name='QuoteFiveRange.buy_price3', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_price4', full_name='QuoteFiveRange.buy_price4', index=3,
      number=4, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_price5', full_name='QuoteFiveRange.buy_price5', index=4,
      number=5, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_volume1', full_name='QuoteFiveRange.buy_volume1', index=5,
      number=6, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_volume2', full_name='QuoteFiveRange.buy_volume2', index=6,
      number=7, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_volume3', full_name='QuoteFiveRange.buy_volume3', index=7,
      number=8, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_volume4', full_name='QuoteFiveRange.buy_volume4', index=8,
      number=9, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='buy_volume5', full_name='QuoteFiveRange.buy_volume5', index=9,
      number=10, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_price1', full_name='QuoteFiveRange.sell_price1', index=10,
      number=11, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_price2', full_name='QuoteFiveRange.sell_price2', index=11,
      number=12, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_price3', full_name='QuoteFiveRange.sell_price3', index=12,
      number=13, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_price4', full_name='QuoteFiveRange.sell_price4', index=13,
      number=14, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_price5', full_name='QuoteFiveRange.sell_price5', index=14,
      number=15, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_polume1', full_name='QuoteFiveRange.sell_polume1', index=15,
      number=16, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_polume2', full_name='QuoteFiveRange.sell_polume2', index=16,
      number=17, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_polume3', full_name='QuoteFiveRange.sell_polume3', index=17,
      number=18, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_polume4', full_name='QuoteFiveRange.sell_polume4', index=18,
      number=19, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='sell_polume5', full_name='QuoteFiveRange.sell_polume5', index=19,
      number=20, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='last_settle', full_name='QuoteFiveRange.last_settle', index=20,
      number=21, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=854,
  serialized_end=1311,
)


_QUOTEMIN = _descriptor.Descriptor(
  name='QuoteMin',
  full_name='QuoteMin',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='QuoteMin.id', index=0,
      number=1, type=4, cpp_type=4, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='time', full_name='QuoteMin.time', index=1,
      number=2, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='price', full_name='QuoteMin.price', index=2,
      number=3, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='volume', full_name='QuoteMin.volume', index=3,
      number=4, type=3, cpp_type=2, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='amount', full_name='QuoteMin.amount', index=4,
      number=5, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='up_down_rate', full_name='QuoteMin.up_down_rate', index=5,
      number=6, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='last_settle', full_name='QuoteMin.last_settle', index=6,
      number=7, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1313,
  serialized_end=1439,
)


_QUOTEKLINE = _descriptor.Descriptor(
  name='QuoteKline',
  full_name='QuoteKline',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='QuoteKline.id', index=0,
      number=1, type=4, cpp_type=4, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='time', full_name='QuoteKline.time', index=1,
      number=2, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='high', full_name='QuoteKline.high', index=2,
      number=3, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='open', full_name='QuoteKline.open', index=3,
      number=4, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='low', full_name='QuoteKline.low', index=4,
      number=5, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='close', full_name='QuoteKline.close', index=5,
      number=6, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='volume', full_name='QuoteKline.volume', index=6,
      number=7, type=3, cpp_type=2, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='amount', full_name='QuoteKline.amount', index=7,
      number=8, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1441,
  serialized_end=1567,
)


_QUOTEINST = _descriptor.Descriptor(
  name='QuoteInst',
  full_name='QuoteInst',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='state', full_name='QuoteInst.state', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='description', full_name='QuoteInst.description', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto2',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1569,
  serialized_end=1624,
)

_RESPONSE.fields_by_name['identification'].message_type = _IDENTIFICATION
_RESPONSE.fields_by_name['heartbeat'].message_type = _HEARTBEAT
_RESPONSE.fields_by_name['quoteDyna'].message_type = _QUOTEDYNA
_RESPONSE.fields_by_name['quoteFiveRange'].message_type = _QUOTEFIVERANGE
_RESPONSE.fields_by_name['quoteMin'].message_type = _QUOTEMIN
_RESPONSE.fields_by_name['klineDay'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineWeek'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineMonth'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineMin1'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineMin5'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineMin15'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineMin30'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineMin60'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['klineMin240'].message_type = _QUOTEKLINE
_RESPONSE.fields_by_name['quoteInst'].message_type = _QUOTEINST
_IDENTIFICATION.fields_by_name['category'].enum_type = _CATEGORY
_IDENTIFICATION.fields_by_name['instType'].enum_type = _INSTTYPE
_QUOTEINST.fields_by_name['state'].enum_type = _STATE
DESCRIPTOR.message_types_by_name['Response'] = _RESPONSE
DESCRIPTOR.message_types_by_name['Identification'] = _IDENTIFICATION
DESCRIPTOR.message_types_by_name['Heartbeat'] = _HEARTBEAT
DESCRIPTOR.message_types_by_name['QuoteDyna'] = _QUOTEDYNA
DESCRIPTOR.message_types_by_name['QuoteFiveRange'] = _QUOTEFIVERANGE
DESCRIPTOR.message_types_by_name['QuoteMin'] = _QUOTEMIN
DESCRIPTOR.message_types_by_name['QuoteKline'] = _QUOTEKLINE
DESCRIPTOR.message_types_by_name['QuoteInst'] = _QUOTEINST
DESCRIPTOR.enum_types_by_name['Category'] = _CATEGORY
DESCRIPTOR.enum_types_by_name['InstType'] = _INSTTYPE
DESCRIPTOR.enum_types_by_name['State'] = _STATE

Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
  DESCRIPTOR = _RESPONSE,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:Response)
  ))
_sym_db.RegisterMessage(Response)

Identification = _reflection.GeneratedProtocolMessageType('Identification', (_message.Message,), dict(
  DESCRIPTOR = _IDENTIFICATION,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:Identification)
  ))
_sym_db.RegisterMessage(Identification)

Heartbeat = _reflection.GeneratedProtocolMessageType('Heartbeat', (_message.Message,), dict(
  DESCRIPTOR = _HEARTBEAT,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:Heartbeat)
  ))
_sym_db.RegisterMessage(Heartbeat)

QuoteDyna = _reflection.GeneratedProtocolMessageType('QuoteDyna', (_message.Message,), dict(
  DESCRIPTOR = _QUOTEDYNA,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:QuoteDyna)
  ))
_sym_db.RegisterMessage(QuoteDyna)

QuoteFiveRange = _reflection.GeneratedProtocolMessageType('QuoteFiveRange', (_message.Message,), dict(
  DESCRIPTOR = _QUOTEFIVERANGE,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:QuoteFiveRange)
  ))
_sym_db.RegisterMessage(QuoteFiveRange)

QuoteMin = _reflection.GeneratedProtocolMessageType('QuoteMin', (_message.Message,), dict(
  DESCRIPTOR = _QUOTEMIN,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:QuoteMin)
  ))
_sym_db.RegisterMessage(QuoteMin)

QuoteKline = _reflection.GeneratedProtocolMessageType('QuoteKline', (_message.Message,), dict(
  DESCRIPTOR = _QUOTEKLINE,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:QuoteKline)
  ))
_sym_db.RegisterMessage(QuoteKline)

QuoteInst = _reflection.GeneratedProtocolMessageType('QuoteInst', (_message.Message,), dict(
  DESCRIPTOR = _QUOTEINST,
  __module__ = 'Market_pb2'
  # @@protoc_insertion_point(class_scope:QuoteInst)
  ))
_sym_db.RegisterMessage(QuoteInst)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), _b('\n#com.goldplusgold.td.market.protobufB\006Market'))
# @@protoc_insertion_point(module_scope)
