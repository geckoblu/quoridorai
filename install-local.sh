#!/bin/bash

abspath=$(cd ${0%/*} && echo $PWD/${0##*/})
script_path=`dirname "$abspath"`

install_jar() {

	mkdir -p ${HOME}/.local/lib/quoridorai
	rm -fR   ${HOME}/.local/lib/quoridorai
    mkdir -p ${HOME}/.local/lib/quoridorai

	JARNAME=`find $1 -name "quoridorai-*.jar" -printf '%f\n'`
	cp ${script_path}/$1/${JARNAME} ${HOME}/.local/lib/quoridorai/

    mkdir -p ${HOME}/.local/bin
    
	cat > ${HOME}/.local/bin/quoridorai << EOF
#!/bin/sh

java -jar ${HOME}/.local/lib/quoridorai/${JARNAME}
EOF

	chmod u+x ~/.local/bin/quoridorai
	
	cp ${script_path}/quoridorai.desktop ${HOME}/.local/share/applications/quoridorai.desktop
    cp ${script_path}/resources/icons/quoridorai.png ${HOME}/.local/share/icons/quoridorai.png

}

JARPATH=.
JARCOUNT=`find ${JARPATH} -name "quoridorai-*.jar" | wc -l`

if [ "$JARCOUNT" == "1" ]; then

	install_jar ${JARPATH}

else

	if [ "$JARCOUNT" == "0" ]; then

		JARPATH=..
		JARCOUNT=`find ${JARPATH} -name "quoridorai-*.jar" | wc -l`

		if [ "$JARCOUNT" == "1" ]; then

			install_jar ${JARPATH}

		elif [ "$JARCOUNT" == "0" ]; then
	        echo "ERROR: No quoridorai jar found".
	        return 1
        else
	        echo "ERROR: Too much quoridorai jar found."
	        return 2
        fi

	else

		echo "ERROR: Too much quoridorai jar found."
		return 2

	fi

fi

