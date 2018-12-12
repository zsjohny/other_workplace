import sys

import jieba

if __name__ == "__main__":
    print(",".join(jieba.cut(sys.argv[1], cut_all=False)))
