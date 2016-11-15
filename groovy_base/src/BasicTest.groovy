import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL

class BasicTest {

	static main(args) {

		def map = [:]

		map."an identifier with a space and double quotes" = "ALLOWED"
		map.'with-dash-signs-and-single-quotes' = "ALLOWED"

		println map."with-dash-signs-and-single-quotes"

		def name = 'Groovy'
		def template = """
		    Dear Mr ${name},
		
		    You're the winner of the lottery!
		
		    Yours sincerly,
		
		    Dave
		"""
		
		def numbers = [1, 2, 3]
		
		def colors = [red: '#FF0000', green: '#00FF00', blue: '#0000FF']
		
		
		if(colors.containsKey("red"))
		{
			println '°üº¬ºìÉ«......................'	
		}
		
		def count = 0
		new SwingBuilder().edt {
		  frame(title: 'Frame', size: [300, 300], show: true) {
			borderLayout()
			textlabel = label(text: 'Click the button!', constraints: BL.NORTH)
			button(text:'Click Me',
				 actionPerformed: {count++; textlabel.text = "Clicked ${count} time(s)."; println "clicked"}, constraints:BL.SOUTH)
		  }
		}
	}
}
