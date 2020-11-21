import React, {Component} from 'react';

class Result extends Component {

  constructor(props) {
    super(props);
    this.state = {
      finish: false,
      title: "MY DAY AT CHUCK-E-CHEESE",
    }
  }

  handleFinish = (e) => {
    this.setState({ finish: true })
  }

  render() {
    return(
      <div>
        {this.state.finish ?
          <div>
            <p>{this.state.title}</p>
            <p>ajflasjdlfajsdljasdlf</p>
            <button onClick={this.props.handleReplay}>PLAY AGAIN</button>
            <button>EXIT LOBBY</button>
          </div>
          :
          <div>
            <p>Great job! You've finished the Mad Lib. Ready to see it?</p>
            <button onClick={this.handleFinish}>FINISH</button>
          </div>
        }
      </div>
    );
  }
}

export default Result