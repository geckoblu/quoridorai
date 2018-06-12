#!/bin/sh

abspath=$(cd ${0%/*} && echo $PWD/${0##*/})
script_path=`dirname "$abspath"`

cp ${script_path}/quoridorai.desktop ${HOME}/.local/share/applications/quoridorai.desktop

cp ${script_path}/resources/icons/quoridorai.png ${HOME}/.local/share/icons/quoridorai.png

mkdir -p ${HOME}/.local/lib/quoridorai
cp ${script_path}/../quoridorai-2.1.jar ${HOME}/.local/lib/quoridorai/quoridorai-2.1.jar

cp ${script_path}/quoridorai ${HOME}/.local/bin/
